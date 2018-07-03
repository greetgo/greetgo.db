package kz.greetgo.db.nf36.core;

public interface SequenceNext {
  long nextLong(String sequenceName);

  int nextInt(String sequenceName);

  int nextInteger(String sequenceName);
}
