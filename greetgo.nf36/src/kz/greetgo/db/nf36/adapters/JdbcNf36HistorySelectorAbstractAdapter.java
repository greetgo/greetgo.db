package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.bridges.ClassAccessor;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.model.SqlLog;
import kz.greetgo.db.nf36.utils.UtilsNf36;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static kz.greetgo.db.nf36.bridges.ClassAccessorStorage.classAccessorStorage;
import static kz.greetgo.db.nf36.utils.SqlConvertUtil.forSql;
import static kz.greetgo.db.nf36.utils.SqlConvertUtil.fromSql;

abstract class JdbcNf36HistorySelectorAbstractAdapter implements Nf36HistorySelector, ConnectionCallback<Boolean> {
  protected boolean selectAuthor = false;
  protected Jdbc jdbc;
  protected SqlLogAcceptor logAcceptor = null;
  protected String ts;

  protected boolean isErrorEnabled() {
    return logAcceptor != null && logAcceptor.isErrorEnabled();
  }

  protected boolean isTraceEnabled() {
    return logAcceptor != null && logAcceptor.isTraceEnabled();
  }

  protected void acceptLog(SqlLog log) {
    if (log != null && logAcceptor != null) {
      logAcceptor.accept(log);
    }
  }

  @Override
  public Nf36HistorySelector setTimeFieldName(String timeFieldName) {
    this.ts = timeFieldName;
    return this;
  }

  protected String insertedAt;

  @Override
  public Nf36HistorySelector setInsertedAtFieldName(String insertedAtFieldName) {
    this.insertedAt = insertedAtFieldName;
    return this;
  }

  protected String nf3TableName;

  @Override
  public Nf36HistorySelector setNf3TableName(String nf3TableName) {
    this.nf3TableName = nf3TableName;
    return this;
  }

  protected final Set<String> usedTableNames = new HashSet<>();

  protected void populateUsedTableNames() {
    usedTableNames.add(nf3TableName);
    for (EntityField field : fieldList) {
      usedTableNames.add(field.nf6TableName);
    }
  }

  protected class EntityField {
    final String nf6TableName;
    final String dbFieldName;
    final String authorFieldName;

    public EntityField(String nf6TableName, String dbFieldName, String authorFieldName) {
      this.nf6TableName = nf6TableName;
      this.dbFieldName = dbFieldName;
      this.authorFieldName = authorFieldName;
    }

    String javaFieldName() {
      String aliasName = fieldAliasMap.get(dbFieldName);
      return aliasName == null ? dbFieldName : aliasName;
    }

    String viewAlias = null;

    String viewAlias() {
      if (viewAlias != null) {
        return viewAlias;
      }

      String name = UtilsNf36.extractAfterDot(nf6TableName);

      String alias = "v_" + name;

      int i = 1;

      while (usedTableNames.contains(alias)) {
        alias = "v" + (i++) + "_" + name;
      }

      usedTableNames.add(alias);
      viewAlias = alias;

      return alias;
    }

    public String commaAuthor() {
      return authorFieldName == null ? "" : ", " + authorFieldName;
    }

    public String leftJoinCondition() {
      return idFieldList.stream()
          .map(f -> f.name)
          .map(name -> nf3TableName + "." + name + " = " + viewAlias() + "." + name)
          .collect(joining(" and "));
    }
  }

  protected final List<EntityField> fieldList = new ArrayList<>();

  @Override
  public Nf36HistorySelector field(String nf6TableName, String dbFieldName, String authorFieldName) {
    fieldList.add(new EntityField(nf6TableName, dbFieldName, authorFieldName));
    return this;
  }

  protected final Map<String, String> fieldAliasMap = new HashMap<>();

  @Override
  public Nf36HistorySelector addFieldAlias(String dbFieldName, String aliasName) {
    fieldAliasMap.put(dbFieldName, aliasName);
    return this;
  }

  protected final Map<String, String> idAliasMap = new HashMap<>();

  class IdField {
    final String name;

    String refAlias = null;

    String javaName = null;

    String javaName() {
      if (javaName != null) {
        return javaName;
      }

      String aliasName = idAliasMap.get(name);
      if (aliasName == null) {
        javaName = name;
      } else {
        javaName = aliasName;
      }

      return javaName;
    }

    public IdField(String name) {
      this.name = name;
    }

    public Object extractValue() {
      return accessor().extractValue(javaName(), destinationObject);
    }
  }

  protected final List<IdField> idFieldList = new ArrayList<>();

  @Override
  public Nf36HistorySelector addId(String idName) {
    idFieldList.add(new IdField(idName));
    return this;
  }

  @Override
  public Nf36HistorySelector addIdAlias(String idName, String idAlias) {
    idAliasMap.put(idName, idAlias);
    return this;
  }

  protected Date at = null;

  @Override
  public Nf36HistorySelector at(Date date) {
    at = date;
    return this;
  }

  private final List<Consumer<Object>> onAbsentConsumers = new ArrayList<>();

  @Override
  public Nf36HistorySelector onAbsent(Consumer<Object> destinationObjectConsumer) {
    onAbsentConsumers.add(destinationObjectConsumer);
    return this;
  }

  private Object destinationObject;
  private ClassAccessor destinationObjectAccessor;

  protected ClassAccessor accessor() {
    if (destinationObjectAccessor != null) {
      return destinationObjectAccessor;
    }
    return destinationObjectAccessor = classAccessorStorage().get(destinationObject.getClass());
  }

  @Override
  public boolean putTo(Object destinationObject) {
    this.destinationObject = destinationObject;
    return jdbc.execute(this);
  }

  private final StringBuilder sql = new StringBuilder();
  private final List<Object> sqlParams = new ArrayList<>();

  @Override
  public Boolean doInConnection(Connection con) throws Exception {

    prepareSqlAndParams();

    long startedAt = System.nanoTime();

    try (PreparedStatement ps = con.prepareStatement(sql.toString())) {

      {
        int index = 1;
        for (Object sqlParam : sqlParams) {
          ps.setObject(index++, sqlParam);
        }
      }

      try (ResultSet rs = ps.executeQuery()) {

        boolean exists = rs.next();

        if (exists) {
          putToDestinationObject(rs);
        } else {
          onAbsentConsumers.forEach(c -> c.accept(destinationObject));
        }

        if (isTraceEnabled()) {
          acceptLog(new SqlLog(sql.toString(), sqlParams, null, System.nanoTime() - startedAt));
        }

        return exists;
      }

    } catch (Exception e) {
      if (isErrorEnabled()) {
        acceptLog(new SqlLog(sql.toString(), sqlParams, e, System.nanoTime() - startedAt));
      }
      throw e;
    }
  }

  private String sqlData = null;

  protected String sqlData() {
    if (sqlData != null) {
      return sqlData;
    }

    String alias = "d";

    int i = 1;

    while (usedTableNames.contains(alias)) {
      alias = "d" + i++;
    }

    usedTableNames.add(alias);
    sqlData = alias;

    return alias;
  }

  private void putToDestinationObject(ResultSet rs) throws SQLException {
    for (EntityField f : fieldList) {

      Object dbValue = rs.getObject(f.dbFieldName);

      Object javaValue = fromSql(dbValue, accessor().setterType(f.javaFieldName()));

      accessor().setValue(destinationObject, f.javaFieldName(), javaValue);

    }
  }

  protected void sqlAppend(String line) {
    sql.append(line).append("\n");
  }

  private String ids() {
    return idFieldList.stream()
        .map(f -> f.name)
        .collect(joining(", "));
  }

  private void prepareSqlAndParams() {
    populateUsedTableNames();

    //
    //INPUT ALIAS
    //

    sqlAppend("with " + sqlData() + " as (");
    sqlAppend("  select " + timestampParameter() + " as t");
    sqlParams.add(new Timestamp(at.getTime()));
    int i = 1;
    for (IdField f : idFieldList) {
      f.refAlias = "i" + i++;
      sqlAppend("       , ? as " + f.refAlias);
      sqlParams.add(forSql(f.extractValue()));
    }
    sqlAppendFromDual();
    sqlAppend(")");

    //
    //FIELD ALIASES
    //

    String viewFilter = ts + " <= " + sqlData() + ".t and " + (

        idFieldList.stream()
            .map(f -> f.name + " = " + sqlData() + "." + f.refAlias)
            .collect(joining(" and "))

    );

    for (EntityField f : fieldList) {
      sqlAppend(", " + f.viewAlias() + " as (");

      String fieldNames = ids() + f.commaAuthor() + ", " + f.dbFieldName;
      String rn = missName(fieldNames, "rn");

      sqlAppend("  select " + fieldNames + " from (");
      sqlAppend("    select " + fieldNames);
      sqlAppend("         , row_number() over (partition by " + ids() + " order by " + ts + " desc) as " + rn);
      sqlAppend("    from " + f.nf6TableName + ", " + sqlData() + " where " + viewFilter);
      sqlAppend("  ) x where x." + rn + " = 1");

      sqlAppend(")");
    }

    //
    // MAIN SELECT
    //

    sqlAppend("");

    sqlAppend("select " + (

        idFieldList.stream()
            .map(f -> nf3TableName + "." + f.name)
            .collect(joining(", "))

    ));

    for (EntityField f : fieldList) {
      sqlAppend("     , " + f.viewAlias() + "." + f.dbFieldName);
    }

    sqlAppend("from " + sqlData() + ", " + nf3TableName);

    for (EntityField f : fieldList) {
      sqlAppend("left join " + f.viewAlias() + " on " + f.leftJoinCondition());
    }

    sqlAppend("where " + nf3TableName + "." + insertedAt + " <= " + sqlData() + ".t and " + idFilter());

  }

  protected abstract void sqlAppendFromDual();

  private String idFilter() {
    return idFieldList.stream()
        .map(f -> nf3TableName + "." + f.name + " = " + sqlData() + "." + f.refAlias)
        .collect(joining(" and "))
        ;
  }

  @SuppressWarnings("SameParameterValue")
  private static String missName(String namesCommaSeparated, String wishName) {
    Set<String> names = Arrays.stream(namesCommaSeparated
        .split(", "))
        .map(String::trim)
        .collect(toSet());

    String ret = wishName;
    int i = 1;

    while (names.contains(ret)) {
      ret = wishName + i++;
    }

    return ret;
  }

  protected abstract String timestampParameter();
}
