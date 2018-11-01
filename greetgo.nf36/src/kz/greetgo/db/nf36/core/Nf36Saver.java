package kz.greetgo.db.nf36.core;

import java.util.function.Predicate;

public interface Nf36Saver {
  // Common methods

  Nf36Saver setNf3TableName(String nf3TableName);

  Nf36Saver setTimeFieldName(String timeFieldName);

  Nf36Saver setAuthorFieldNames(String nf3CreatedBy, String nf3ModifiedBy, String nf6InsertedBy);

  Nf36Saver addIdName(String idName);

  Nf36Saver addFieldName(String nf6TableName, String fieldName);

  Nf36Saver putUpdateToNow(String timestampFieldName);

  // Field preset methods

  Nf36Saver setAuthor(Object author);

  Nf36Saver presetValue(String fieldName, Object value);

  Nf36Saver addSkipIf(String fieldName, Predicate<?> predicate);

  Nf36Saver addAlias(String fieldName, String alias);

  // Main operation

  void save(Object objectWithData);
}
