package kz.greetgo.db.nf36.core;

public interface SequenceNext {
  long nextLong(String sequenceName);

  int nextInt(String sequenceName);

  default int nextInteger(String sequenceName) {
    return nextInt(sequenceName);
  }
}
