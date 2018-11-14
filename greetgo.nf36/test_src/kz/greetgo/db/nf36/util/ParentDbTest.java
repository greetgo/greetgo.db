package kz.greetgo.db.nf36.util;

import kz.greetgo.db.worker.connector.DatabaseConnector;

import static kz.greetgo.db.worker.connector.DatabaseConnectorBuilder.newDatabaseConnectorBuilder;

public class ParentDbTest {

  private DatabaseConnector connector = null;

  protected DatabaseConnector connector() {
    if (connector != null) {
      return connector;
    }

    return connector = newDatabaseConnectorBuilder()
        .setDbType(getClass().getAnnotation(Use.class).value())
        .setDbName(System.getProperty("user.name") + "_db_security")
        .setDbUser(System.getProperty("user.name") + "_db_security")
        .setDbPassword("1111")
        .build()
        ;
  }


}
