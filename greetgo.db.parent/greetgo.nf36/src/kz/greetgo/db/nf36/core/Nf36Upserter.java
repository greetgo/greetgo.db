package kz.greetgo.db.nf36.core;

public interface Nf36Upserter {
  void setTableName(String tableName);

  void putId(String idName, Object idValue);

  void putField(String fieldName, Object fieldValue);

  void commit();

  void setNf3Prefix(String nf3prefix);

  void setNf6Prefix(String nf6prefix);
}
