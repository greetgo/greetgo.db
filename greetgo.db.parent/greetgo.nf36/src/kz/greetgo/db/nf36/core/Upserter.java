package kz.greetgo.db.nf36.core;

public interface Upserter {
  void setTableName(String tableName);

  void putId(String idName, Object idValue);

  void putField(String fieldName, Object fieldValue);

  void go();

  void setNf3Prefix(String nf3prefix);

  void setNf6Prefix(String nf6prefix);
}
