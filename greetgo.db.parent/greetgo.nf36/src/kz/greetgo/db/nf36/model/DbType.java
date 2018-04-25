package kz.greetgo.db.nf36.model;

public interface DbType {
  SqlType sqlType();

  int len();

  boolean nullable();
}
