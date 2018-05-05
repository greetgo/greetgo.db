package kz.greetgo.db.nf36.adapters;

import java.sql.Connection;

class JdbcNf36UpserterAdapterOracle extends JdbcNf36UpserterAbstractAdapter {
  @Override
  protected JdbcNf36UpserterAbstractAdapter copyInstance() {
    return new JdbcNf36UpserterAdapterOracle();
  }

  @Override
  protected void upsert(Connection con) throws Exception {
    throw new RuntimeException("While not implemented");
  }
}
