package kz.greetgo.db.worker.connector;

import kz.greetgo.db.worker.util.OracleUtil;

public class DatabaseConnectorOracle extends DatabaseConnectorAbstract {
  @Override
  public void prepareDatabase() throws Exception {
    OracleUtil.recreateDb(dbUser, dbPassword);
  }
}
