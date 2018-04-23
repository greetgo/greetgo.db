package kz.greetgo.db.nf36.model;

import java.util.List;

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

  List<String> referenceDbNames();

  Nf3Table referenceTo();

  boolean hasNextPart();

  boolean notNullAndNotPrimitive();
}
