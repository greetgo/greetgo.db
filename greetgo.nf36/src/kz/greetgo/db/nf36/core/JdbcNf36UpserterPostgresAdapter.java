package kz.greetgo.db.nf36.core;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.model.SqlLog;
import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Stream.concat;

public class JdbcNf36UpserterPostgresAdapter implements Nf36Upserter, ConnectionCallback<Void> {
  private final Jdbc jdbc;

  private final SqlLogAcceptor logAcceptor;
  private String nf3TableName;
  private String timeFieldName;
  private String nf3CreatedBy = null;
  private String nf3ModifiedBy = null;
  private String nf6InsertedBy = null;

  private Object author = null;

  private final Map<String, Object> idValueMap = new HashMap<>();
  private final Map<String, Object> fieldValueMap = new HashMap<>();
  private final Map<String, Object> nf6ValueMap = new HashMap<>();
  private final List<String> toNowFieldList = new ArrayList<>();

  JdbcNf36UpserterPostgresAdapter parent = null;

  @Override
  public Nf36Upserter more() {
    JdbcNf36UpserterPostgresAdapter ret = new JdbcNf36UpserterPostgresAdapter(jdbc, logAcceptor);
    ret.nf3TableName = nf3TableName;
    ret.timeFieldName = timeFieldName;
    ret.nf3CreatedBy = nf3CreatedBy;
    ret.nf3ModifiedBy = nf3ModifiedBy;
    ret.nf6InsertedBy = nf6InsertedBy;
    ret.author = author;
    ret.parent = this;
    return ret;
  }

  public JdbcNf36UpserterPostgresAdapter(Jdbc jdbc, SqlLogAcceptor logAcceptor) {
    if (jdbc == null) throw new IllegalArgumentException("jdbc cannot be null");
    this.jdbc = jdbc;
    this.logAcceptor = logAcceptor;
  }

  public JdbcNf36UpserterPostgresAdapter setAuthor(String author) {
    this.author = author;
    return this;
  }

  @Override
  public void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy) {
    this.nf3CreatedBy = nf3CreatedBy;
    this.nf3ModifiedBy = nf3ModifiedBy;
    this.nf6InsertedBy = nf6InsertedBy;
  }

  @Override
  public void setTimeFieldName(String timeFieldName) {
    this.timeFieldName = timeFieldName;
  }

  @Override
  public void setNf3TableName(String nf3TableName) {
    this.nf3TableName = nf3TableName;
  }

  @Override
  public void putUpdateToNow(String fieldName) {
    toNowFieldList.add(fieldName);
  }

  @Override
  public void putUpdateToNowWithParent(String fieldName) {
    if (parent != null) parent.putUpdateToNowWithParent(fieldName);
    toNowFieldList.add(fieldName);
  }

  @Override
  public void putId(String idName, Object idValue) {
    idValueMap.put(idName, SqlConvertUtil.forSql(idValue));
  }

  @Override
  public void putField(String nf6TableName, String fieldName, Object fieldValue) {
    fieldValueMap.put(fieldName, SqlConvertUtil.forSql(fieldValue));
    nf6ValueMap.put(nf6TableName + ";" + fieldName, SqlConvertUtil.forSql(fieldValue));
  }

  @Override
  public void commit() {
    jdbc.execute(this);
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {

    boolean autoCommit = con.getAutoCommit();

    if (autoCommit) con.setAutoCommit(false);

    try {
      execute(con);
      if (autoCommit) con.commit();
    } catch (Exception e) {
      if (autoCommit) con.rollback();
      throw e;
    } finally {
      if (autoCommit) con.setAutoCommit(true);
    }

    return null;
  }

  private void execute(Connection con) throws Exception {
    if (parent != null) parent.execute(con);
    upsert(con);
    insertHistory(con);
  }

  private void insertHistory(Connection con) throws Exception {
    Map<String, List<Map.Entry<String, Object>>> collect = nf6ValueMap.entrySet().stream()
      .collect(Collectors.groupingBy(e -> e.getKey().split(";")[0]));

    for (Map.Entry<String, List<Map.Entry<String, Object>>> e : collect.entrySet()) {
      Map<String, Object> nf6values = new HashMap<>();
      for (Map.Entry<String, Object> e1 : e.getValue()) {
        nf6values.put(e1.getKey().split(";")[1], e1.getValue());
      }
      insertNf6IfNeed(e.getKey(), nf6values, con);
    }
  }


  private void upsert(Connection con) throws Exception {
    String insertNames = "";
    String insertQ = "";
    String updateNamesQ = "";
    Stream<Object> insertStream = null;
    Stream<Object> updateStream = null;

    if (nf3CreatedBy != null) {
      insertNames = ", " + nf3CreatedBy + ", " + nf3ModifiedBy;
      insertQ = ", ?, ?";
      insertStream = Stream.of(author, author);
      updateNamesQ = ", " + nf3ModifiedBy + " = ?";
      updateStream = Stream.of(author);
    }

    String sql = "insert into " + nf3TableName + " ("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + insertNames
      + ") values ("
      + idValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + insertQ
      + ") on conflict ("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ") do update set "
      + fieldValueMap.keySet().stream().sorted().map(k -> k + " = ?").collect(Collectors.joining(", "))
      + (
      toNowFieldList.isEmpty()
        ? ""
        : ", " + toNowFieldList.stream().map(n -> n + " = clock_timestamp()").collect(Collectors.joining(", "))
        + updateNamesQ
    );

    Stream<Object> s = idValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue);
    s = concat(s, fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue));
    if (insertStream != null) s = concat(s, insertStream);
    s = concat(s, fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue));
    if (updateStream != null) s = concat(s, updateStream);

    executeUpdate(con, sql, s.collect(Collectors.toList()));
  }

  private void executeUpdate(Connection con, String sql, List<Object> params) throws Exception {
    long startedAt = System.nanoTime();

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      int index = 1;
      for (Object param : params) {
        ps.setObject(index++, param);
      }

      ps.executeUpdate();

      long delay = System.nanoTime() - startedAt;
      if (logAcceptor != null && logAcceptor.isTraceEnabled()) {
        logAcceptor.accept(new SqlLog(sql, unmodifiableList(params), null, delay));
      }
    } catch (Exception e) {
      long delay = System.nanoTime() - startedAt;
      if (logAcceptor != null && logAcceptor.isErrorEnabled()) {
        logAcceptor.accept(new SqlLog(sql, unmodifiableList(params), e, delay));
      }
      throw e;
    }
  }

  private void insertNf6IfNeed(String nf6TableName, Map<String, Object> values, Connection con) throws Exception {

    List<String> fieldNames = new ArrayList<>();
    List<Object> fieldValues = new ArrayList<>();

    values.entrySet().stream()
      .sorted(Comparator.comparing(Map.Entry::getKey))
      .forEachOrdered(e -> {
        fieldNames.add(e.getKey());
        fieldValues.add(e.getValue());
      });

    Optional<List<Object>> current = selectCurrent(nf6TableName, fieldNames, con);

    if (current.isPresent() && eq(current.get(), fieldValues)) return;

    insertNf6(nf6TableName, fieldNames, fieldValues, con);
  }

  private Optional<List<Object>> selectCurrent(String nf6TableName, List<String> fieldNames,
                                               Connection con) throws SQLException {

    List<Object> idValueList = idValueMap.entrySet().stream()
      .sorted(comparing(Map.Entry::getKey))
      .map(Map.Entry::getValue)
      .collect(Collectors.toList());

    String keyEquals = idValueMap.keySet().stream()
      .sorted()
      .map(n -> n + " = ?")
      .collect(Collectors.joining(" and "));

    long startedAt = System.nanoTime();

    String sql = "select " + fieldNames.stream().collect(Collectors.joining(", "))
      + " from " + nf6TableName
      + " where " + keyEquals
      + " order by " + timeFieldName + " desc limit 1";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      int index = 1;
      for (Object value : idValueList) {
        ps.setObject(index++, value);
      }

      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) return Optional.empty();

        List<Object> params = new ArrayList<>();
        for (int i = 1, c = fieldNames.size(); i <= c; i++) {
          params.add(rs.getObject(i));
        }

        long delay = System.nanoTime() - startedAt;
        if (logAcceptor != null && logAcceptor.isTraceEnabled()) {
          logAcceptor.accept(new SqlLog(sql, unmodifiableList(idValueList), null, delay));
        }

        return Optional.of(params);

      }
    } catch (Exception e) {
      long delay = System.nanoTime() - startedAt;
      if (logAcceptor != null && logAcceptor.isErrorEnabled()) {
        logAcceptor.accept(new SqlLog(sql, unmodifiableList(idValueList), e, delay));
      }
      throw e;
    }
  }

  private static boolean eq(List<Object> list1, List<Object> list2) {
    return Objects.equals(list1, list2);
  }

  private void insertNf6(String nf6TableName, List<String> fieldNames, List<Object> fieldValues,
                         Connection con) throws Exception {

    String insName = "";
    String insQ = "";
    Stream<Object> insStream = null;

    if (nf6InsertedBy != null) {
      insName = ", " + nf6InsertedBy;
      insQ = ", ?";
      insStream = Stream.of(author);
    }

    String sql = "insert into " + nf6TableName + " (" + (

      idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))

    ) + ", " + (

      fieldNames.stream().collect(Collectors.joining(", "))

    ) + insName + ") values (" + (

      idValueMap.keySet().stream().map(n -> "?").collect(Collectors.joining(", "))

    ) + ", " + (

      fieldNames.stream().map(n -> "?").collect(Collectors.joining(", "))

    ) + insQ + ")";

    Stream<Object> s = idValueMap.entrySet().stream()
      .sorted(Comparator.comparing(Map.Entry::getKey))
      .map(Map.Entry::getValue);

    s = concat(s, fieldValues.stream());
    if (insStream != null) s = concat(s, insStream);

    executeUpdate(con, sql, s.collect(Collectors.toList()));
  }
}
