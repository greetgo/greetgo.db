package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

abstract class JdbcSequenceNextAbstractAdapter implements SequenceNext {
  Jdbc jdbc;
  SqlLogAcceptor logAcceptor;

  @Override
  public long nextLong(String sequenceName) {
    return 0;
  }

  @Override
  public int nextInt(String sequenceName) {
    return 0;
  }
}
