package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.Sequence;

import java.lang.reflect.Field;

public interface SqlDialect {
  String createFieldDefinition(DbType dbType, String name, Field field, Object definer) throws Exception;

  void checkObjectName(String objectName, ObjectNameType objectNameType);

  String fieldTimestampWithDefaultNow(String fieldName);

  String createAuthorFieldDefinition(AuthorField authorField);

  String createSequence(Sequence sequence);
}
