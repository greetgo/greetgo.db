package kz.greetgo.db.nf36.adapters;

class JdbcNf36HistorySelectorAdapterOracle extends JdbcNf36HistorySelectorAbstractAdapter {
  @Override
  protected String timestampParameter() {
    return "?";
  }

  @Override
  protected void sqlAppendFromDual() {
    sqlAppend("from dual");
  }
}
