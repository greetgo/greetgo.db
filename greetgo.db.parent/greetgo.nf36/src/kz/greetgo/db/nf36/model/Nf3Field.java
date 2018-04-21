package kz.greetgo.db.nf36.model;

public interface Nf3Field {

  boolean isId();

  int idOrder();

  String javaName();

  String dbName();

  Class<?> javaType();

  DbType dbType();

  Class<?> referenceTo();

  String nextPart();

  boolean isReference();

  boolean hasNextPart();
}
