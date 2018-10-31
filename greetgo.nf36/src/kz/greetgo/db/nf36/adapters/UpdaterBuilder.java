package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;

public class UpdaterBuilder {
  private SqlLogAcceptor logAcceptor = null;
  private Jdbc jdbc = null;

  public UpdaterBuilder setJdbc(Jdbc jdbc) {
    this.jdbc = jdbc;
    return this;
  }

  public UpdaterBuilder setLogAcceptor(SqlLogAcceptor logAcceptor) {
    this.logAcceptor = logAcceptor;
    return this;
  }

  private boolean hasAuthor = false;
  private Object author;

  public UpdaterBuilder setAuthor(Object author) {
    hasAuthor = true;
    this.author = author;
    return this;
  }

  private JdbcNf36UpdaterAbstractAdapter adapter = null;

  public UpdaterBuilder database(DbType dbType) {
    switch (dbType) {
      case Postgres:
        adapter = new JdbcNf36UpdaterAdapterPostgres();
        return this;

      case Oracle:
        adapter = new JdbcNf36UpdaterAdapterOracle();
        return this;

      default:
        throw new IllegalArgumentException("Database " + dbType + " is not supported");
    }
  }

  public Nf36Updater build() {
    if (adapter == null) {
      throw new RuntimeException("Please define database. Call method 'database'");
    }
    if (jdbc == null) {
      throw new RuntimeException("Please set jdbc");
    }
    adapter.jdbc = jdbc;
    adapter.logAcceptor = logAcceptor;
    if (hasAuthor) {
      adapter.setAuthor(author);
    }
    return adapter;
  }
}
