package kz.greetgo.db.nf36.db;

public abstract class DatabaseConnectorAbstract implements DatabaseConnector {
  protected String dbName;
  protected String dbUser;

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }
}
