package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

public class WhereUpdaterBuilder {
  private SqlLogAcceptor logAcceptor = null;
  private Jdbc jdbc = null;

  public WhereUpdaterBuilder setJdbc(Jdbc jdbc) {
    this.jdbc = jdbc;
    return this;
  }

  public WhereUpdaterBuilder setLogAcceptor(SqlLogAcceptor logAcceptor) {
    this.logAcceptor = logAcceptor;
    return this;
  }

  private JdbcNf36WhereUpdaterAbstractAdapter adapter = null;

  public WhereUpdaterBuilder database(DbType dbType) {
    switch (dbType) {
      case Postgres:
        adapter = new JdbcNf36WhereUpdaterAdapterPostgres();
        return this;

      default:
        throw new IllegalArgumentException("Database " + dbType + " is not supported");
    }
  }

  public Nf36WhereUpdater build() {
    if (adapter == null) throw new RuntimeException("Please define database. Call method 'database'");
    if (jdbc == null) throw new RuntimeException("Please set jdbc");
    adapter.jdbc = jdbc;
    adapter.logAcceptor = logAcceptor;
    return adapter;
  }
}
