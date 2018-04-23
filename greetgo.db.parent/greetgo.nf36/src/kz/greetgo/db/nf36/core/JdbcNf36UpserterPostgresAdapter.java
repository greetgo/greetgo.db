package kz.greetgo.db.nf36.core;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.model.SqlLog;
import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

public class JdbcNf36UpserterPostgresAdapter implements Nf36Upserter, ConnectionCallback<Void> {

  private final Jdbc jdbc;
  private final SqlLogAcceptor logAcceptor;
  private String nf3TableName;

  public JdbcNf36UpserterPostgresAdapter(Jdbc jdbc, SqlLogAcceptor logAcceptor) {
    if (jdbc == null) throw new IllegalArgumentException("jdbc cannot be null");
    this.jdbc = jdbc;
    this.logAcceptor = logAcceptor;
  }

  @Override
  public void setNf3TableName(String nf3TableName) {
    this.nf3TableName = nf3TableName;
  }

  private final Map<String, Object> idValueMap = new HashMap<>();
  private final Map<String, Object> fieldValueMap = new HashMap<>();

  @Override
  public void putId(String idName, Object idValue) {
    idValueMap.put(idName, SqlConvertUtil.forSql(idValue));
  }

  @Override
  public void putField(String nf6TableName, String fieldName, Object fieldValue) {
    fieldValueMap.put(fieldName, SqlConvertUtil.forSql(fieldValue));
  }

  @Override
  public void commit() {
    jdbc.execute(this);
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {

    String sql = "insert into " + nf3TableName + "("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ") values ("
      + idValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + ", "
      + fieldValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "))
      + ") on conflict ("
      + idValueMap.keySet().stream().sorted().collect(Collectors.joining(", "))
      + ") do update set "
      + fieldValueMap.keySet().stream().sorted().map(k -> k + " = ?").collect(Collectors.joining(", "))
      + "";

    List<Object> params = Stream.concat(
      idValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue),
      Stream.concat(
        fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue),
        fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue)
      )
    ).collect(Collectors.toList());

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

    return null;
  }
}
