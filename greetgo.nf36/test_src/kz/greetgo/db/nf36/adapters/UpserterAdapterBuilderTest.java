package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.nf36.db.DatabaseConnector;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static kz.greetgo.db.nf36.db.DatabaseConnectorBuilder.newDatabaseConnectorBuilder;
import static org.fest.assertions.api.Assertions.assertThat;

public class UpserterAdapterBuilderTest {

  private DatabaseConnector createConnector(DbType dbType) {
    return newDatabaseConnectorBuilder()
        .setDbType(dbType)
        .setDbName(System.getProperty("user.name") + "_db_security")
        .setDbUser(System.getProperty("user.name") + "_db_security")
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
  public void name(DatabaseConnector databaseConnector) {
    databaseConnector.prepareDatabase();
    System.out.println(databaseConnector);
    assertThat(1);
  }
}