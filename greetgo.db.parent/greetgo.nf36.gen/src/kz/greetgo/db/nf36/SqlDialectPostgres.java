package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.SqlType;

public class SqlDialectPostgres implements SqlDialect {
  @Override
  public String createFieldDefinition(DbType dbType, String name) {
    String type = extractType(dbType.sqlType(), dbType.len());
    String def = dbType.defaultNow() ? " not null default clock_timestamp()" : "";
    return name + " " + type + def;
  }

  @Override
  public void checkObjectName(String objectName, ObjectNameType objectNameType) {}

  private String extractType(SqlType sqlType, int len) {
    switch (sqlType) {
      case TIMESTAMP:
        return "TIMESTAMP";
      case VARCHAR:
        return "VARCHAR(" + len + ")";
      case INT:
        return "INT" + len;
      case BOOL:
        return "BOOL";
      case BIGINT:
        return "BIGINT";
      case CLOB:
        return "TEXT";
      default:
        throw new IllegalArgumentException("Cannot create type for " + sqlType);
    }
  }
}
