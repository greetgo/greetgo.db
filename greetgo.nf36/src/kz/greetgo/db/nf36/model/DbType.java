package kz.greetgo.db.nf36.model;

public interface DbType {
  SqlType sqlType();

  int len();

  int scale();

  boolean nullable();

  boolean sequential();
}
