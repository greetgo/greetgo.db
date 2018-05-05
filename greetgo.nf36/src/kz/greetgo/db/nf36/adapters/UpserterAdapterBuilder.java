package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

public class UpserterAdapterBuilder {
  private SqlLogAcceptor logAcceptor = null;
  private Jdbc jdbc = null;

  public UpserterAdapterBuilder setJdbc(Jdbc jdbc) {
    this.jdbc = jdbc;
    return this;
  }

  public UpserterAdapterBuilder setLogAcceptor(SqlLogAcceptor logAcceptor) {
    this.logAcceptor = logAcceptor;
    return this;
  }

  private JdbcNf36UpserterAbstractAdapter adapter = null;

  public UpserterAdapterBuilder database(DbType dbType) {
    switch (dbType) {
      case Postgres:
        adapter = new JdbcNf36UpserterAdapterPostgres();
        return this;

      case Oracle:
        adapter = new JdbcNf36UpserterAdapterOracle();
        return this;

      default:
        throw new IllegalArgumentException("Database " + dbType + " is not supported");
    }
  }

  public Nf36Upserter build() {
    if (adapter == null) throw new RuntimeException("Please define database. Call method 'database'");
    if (jdbc == null) throw new RuntimeException("Please set jdbc");
    adapter.jdbc = jdbc;
    adapter.logAcceptor = logAcceptor;
    return adapter;
  }
}
