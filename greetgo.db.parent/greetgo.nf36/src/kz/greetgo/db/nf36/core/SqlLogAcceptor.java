package kz.greetgo.db.nf36.core;

import kz.greetgo.db.nf36.model.SqlLog;

public interface SqlLogAcceptor {
  boolean isTraceEnabled();

  boolean isErrorEnabled();

  void accept(SqlLog sqlLog);
}
