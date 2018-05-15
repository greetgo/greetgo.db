package kz.greetgo.db.nf36.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public interface Nf3Field {

  boolean isId();

  int idOrder();

  String javaName();

  String dbName();

  Class<?> javaType();

  DbType dbType();

  Class<?> referenceToClass();

  String nextPart();

  boolean isReference();

  boolean isRootReference();

  List<Nf3Field> referenceFields();

  Nf3Field rootField();

  Object definer();

  Field source();

  default List<String> referenceDbNames() {
    return referenceFields().stream().map(Nf3Field::dbName).collect(Collectors.toList());
  }

  Nf3Table referenceTo();

  boolean hasNextPart();

  boolean notNullAndNotPrimitive();

  String commentQuotedForSql();
}