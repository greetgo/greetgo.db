package kz.greetgo.db.worker.connector;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.JdbcOneConnection;
import kz.greetgo.db.worker.util.OracleUtil;

public class DatabaseConnectorOracle extends DatabaseConnectorAbstract {
  @Override
  public void prepareDatabase() throws Exception {
    if (OracleUtil.ping(dbUser, dbPassword)) {
      return;
    }
    OracleUtil.recreateDb(dbUser, dbPassword);
  }

  @Override
  public DbType type() {
    return DbType.Oracle;
  }

  @Override
  public Jdbc jdbc() {
    return new JdbcOneConnection(OracleUtil.getConnection(dbUser, dbPassword));
  }
}
