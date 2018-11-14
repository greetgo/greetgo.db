package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.worker.connector.DatabaseConnector;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static kz.greetgo.db.worker.connector.DatabaseConnectorBuilder.newDatabaseConnectorBuilder;
import static org.fest.assertions.api.Assertions.assertThat;

public class UpserterAdapterBuilderTest {

  private DatabaseConnector createConnector(DbType dbType) {
    return newDatabaseConnectorBuilder()
        .setDbType(dbType)
        .setDbName(System.getProperty("user.name") + "_db_security")
        .setDbUser(System.getProperty("user.name") + "_db_security")
        .setDbPassword("111")
        .build()
        ;
  }

  @DataProvider
  public Object[][] connectorDataProvider() {
    return new Object[][]{
        {createConnector(DbType.Postgres)},
        {createConnector(DbType.Oracle)},
    };
  }

  @Test(dataProvider = "connectorDataProvider")
  public void prepareDb(DatabaseConnector databaseConnector) throws Exception {
    databaseConnector.prepareDatabase();
    System.out.println(databaseConnector);
    assertThat(1);
  }
}
