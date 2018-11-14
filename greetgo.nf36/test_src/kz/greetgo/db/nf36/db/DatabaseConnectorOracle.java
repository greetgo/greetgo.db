package kz.greetgo.db.nf36.db;

import org.testng.SkipException;

public class DatabaseConnectorOracle extends DatabaseConnectorAbstract {
  @Override
  public void prepareDatabase() {
    throw new SkipException("No Oracle driver");
  }
}
