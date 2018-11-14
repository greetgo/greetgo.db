package kz.greetgo.db.nf36.db;

public abstract class DatabaseConnectorAbstract implements DatabaseConnector {
  protected String dbName;

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }
}
