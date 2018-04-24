package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.DbType;

public interface SqlDialect {
  String createFieldDefinition(DbType dbType, String name);

  void checkObjectName(String objectName, ObjectNameType objectNameType);

  String fieldTimestampWithDefaultNow(String fieldName);
}
