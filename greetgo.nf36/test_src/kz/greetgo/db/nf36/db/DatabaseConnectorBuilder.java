package kz.greetgo.db.nf36.db;

import kz.greetgo.db.DbType;

public class DatabaseConnectorBuilder {
  public static DatabaseConnectorBuilder newDatabaseConnectorBuilder() {
    return new DatabaseConnectorBuilder();
  }

  private DatabaseConnectorBuilder() {}

  private DbType dbType = null;

  public DatabaseConnectorBuilder setDbType(DbType dbType) {
    this.dbType = dbType;
    return this;
  }

  public DatabaseConnector build() {
    DatabaseConnectorAbstract ret = preBuild();
    ret.setDbName(dbName);
    return ret;
  }

  private DatabaseConnectorAbstract preBuild() {
    if (dbType == null) {
      throw new RuntimeException("Please define dbType");
    }

    switch (dbType) {
      case Postgres:
        return new DatabaseConnectorPostgres();

      case Oracle:
        return new DatabaseConnectorOracle();

      default:
        throw new RuntimeException("Database " + dbType + " not supported");
    }
  }

  private String dbName = null;

  public DatabaseConnectorBuilder setDbName(String dbName) {
    this.dbName = dbName;
    return this;
  }
}
