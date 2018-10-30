package kz.greetgo.db.nf36.core;

import java.util.function.Predicate;

public interface Nf36Saver {
  // Common methods

  void setNf3TableName(String nf3TableName);

  void setTimeFieldName(String timeFieldName);

  void setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy);

  void addIdName(String idName);

  void addFieldName(String nf6TableName, String fieldName);

  // Field preset methods

  void presetValue(String fieldName, Object value);

  void addSkipIf(String fieldName, Predicate<?> predicate);

  void addAlias(String fieldName, String alias);

  // Main operation

  void save(Object objectWithData);
}
