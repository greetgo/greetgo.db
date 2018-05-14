package nf36_example_with_depinject.util;

import kz.greetgo.db.worker.DbConfig;
import nf36_example_with_depinject.env.DbParamsOracle;

public class DbConfigOracle implements DbConfig {

  private DbConfigOracle() {}

  private static final DbConfigOracle instance = new DbConfigOracle();

  public static DbConfigOracle instance() {
    return instance;
  }

  @Override
  public String url() {
    return DbParamsOracle.url;
  }

  @Override
  public String username() {
    return DbParamsOracle.username;
  }

  @Override
  public String password() {
    return DbParamsOracle.password;
  }

  @Override
  public String dbName() {
    throw new RuntimeException("Not implemented");
  }
}
