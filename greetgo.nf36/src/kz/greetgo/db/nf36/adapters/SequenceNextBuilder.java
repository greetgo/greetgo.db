package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

public class SequenceNextBuilder {
  private SqlLogAcceptor logAcceptor = null;
  private Jdbc jdbc = null;

  public SequenceNextBuilder setJdbc(Jdbc jdbc) {
    this.jdbc = jdbc;
    return this;
  }

  public SequenceNextBuilder setLogAcceptor(SqlLogAcceptor logAcceptor) {
    this.logAcceptor = logAcceptor;
    return this;
  }

  private JdbcSequenceNextAbstractAdapter adapter = null;

  public SequenceNextBuilder database(DbType dbType) {
    switch (dbType) {
      case Postgres:
        adapter = new JdbcSequenceNextAdapterPostgres();
        return this;

      case Oracle:
        adapter = new JdbcSequenceNextAdapterOracle();
        return this;

      default:
        throw new IllegalArgumentException("Database " + dbType + " is not supported");
    }
  }

  public SequenceNext build() {
    if (jdbc == null) {
      throw new RuntimeException("Please set jdbc");
    }
    if (adapter == null) {
      throw new RuntimeException("Please define database. Call method 'database'");
    }
    adapter.jdbc = jdbc;
    adapter.logAcceptor = logAcceptor;
    return adapter;
  }
}
