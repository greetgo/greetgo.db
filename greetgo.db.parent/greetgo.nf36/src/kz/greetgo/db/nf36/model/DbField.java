package kz.greetgo.db.nf36.model;

public interface DbField {
  String name();

  DbFieldType type();

  int len();

  boolean currentTimestamp();

}
