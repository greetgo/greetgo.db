package nf36_example_with_depinject.util;

import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.model.SqlLog;

public class MySqlLogAcceptor implements SqlLogAcceptor {
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
    System.out.println(sqlLog.toStr(true, true, true));
  }
}
