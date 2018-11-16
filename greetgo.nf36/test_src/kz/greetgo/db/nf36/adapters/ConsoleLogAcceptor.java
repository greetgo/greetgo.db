package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.model.SqlLog;

public class ConsoleLogAcceptor implements SqlLogAcceptor {
  @Override
  public boolean isTraceEnabled() {
    return true;
  }

  @Override
  public boolean isErrorEnabled() {
    return true;
  }

  @Override
  public void accept(SqlLog sqlLog) {
    System.out.println(sqlLog.toStr(false, true, false));
  }
}
