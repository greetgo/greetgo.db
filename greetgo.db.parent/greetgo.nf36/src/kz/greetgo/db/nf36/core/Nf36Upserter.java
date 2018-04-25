package kz.greetgo.db.nf36.core;

public interface Nf36Upserter {
  void setNf3TableName(String tableName);

  void setTimeFieldName(String timeFieldName);

  void putId(String idName, Object idValue);

  void putField(String nf6TableName, String fieldName, Object fieldValue);

  void putUpdateToNow(String fieldName);

  void commit();
}
