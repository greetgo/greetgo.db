package kz.greetgo.db.nf36.core;

public interface Nf36Upserter {
  void setNf3TableName(String tableName);

  void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy);

  Nf36Upserter setAuthor(Object author);

  void setTimeFieldName(String timeFieldName);

  void putId(String idName, Object idValue);

  void putField(String nf6TableName, String fieldName, Object fieldValue);

  void putUpdateToNow(String fieldName);

  void putUpdateToNowWithParent(String fieldName);

  Nf36Upserter more();

  void commit();
}
