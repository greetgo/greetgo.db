package kz.greetgo.db.nf36.adapters;

class JdbcSequenceNextAdapterPostgres extends JdbcSequenceNextAbstractAdapter {
  @Override
  protected String selectSequence(String sequenceName) {
    return "select nextVal('" + sequenceName + "')";
  }
}
