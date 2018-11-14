package kz.greetgo.db.worker.connector;

public abstract class DatabaseConnectorAbstract implements DatabaseConnector {
  protected String dbName;
  protected String dbUser;
  protected String dbPassword;

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }
}
