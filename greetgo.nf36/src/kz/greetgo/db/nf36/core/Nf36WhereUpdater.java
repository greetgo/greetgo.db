package kz.greetgo.db.nf36.core;

public interface Nf36WhereUpdater {

  void setNf3TableName(String tableName);

  void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy);

  Nf36WhereUpdater setAuthor(Object author);

  void setTimeFieldName(String timeFieldName);

  void setIdFieldNames(String... idFieldNames);

  void updateFieldToNow(String fieldName);

  void setField(String nf6TableName, String fieldName, Object fieldValue);

  void where(String fieldName, Object fieldValue);

  void commit();
}
