package kz.greetgo.db.worker.connector;

import kz.greetgo.db.AbstractJdbcWithDataSource;
import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.TransactionManager;
import kz.greetgo.db.worker.util.PostgresUtil;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicReference;

class DatabaseConnectorPostgres extends DatabaseConnectorAbstract {
  @Override
  public void prepareDatabase() throws Exception {
    if (PostgresUtil.ping(dbName, dbUser, dbPassword)) {
      return;
    }

    PostgresUtil.recreateDb(dbName, dbUser, dbPassword);
  }

  @Override
  public DbType type() {
    return DbType.Postgres;
  }

  private final AtomicReference<Jdbc> jdbcHolder = new AtomicReference<>(null);

  @Override
  public Jdbc jdbc() {
    {
      Jdbc jdbc = jdbcHolder.get();
      if (jdbc != null) {
        return jdbc;
      }
    }

    synchronized (this) {
      {
        Jdbc jdbc = jdbcHolder.get();
        if (jdbc != null) {
          return jdbc;
        }
      }
      {
        Jdbc jdbc = new AbstractJdbcWithDataSource() {
          DataSource dataSource = PostgresUtil.createDataSource(dbName, dbUser, dbPassword);

          @Override
          protected DataSource getDataSource() {
            return dataSource;
          }

          @Override
          protected TransactionManager getTransactionManager() {
            return null;
          }
        };
        jdbcHolder.set(jdbc);
        return jdbc;
      }
    }
  }
}
