package nf36_example_with_depinject.util;

import kz.greetgo.db.worker.DbConfig;
import nf36_example_with_depinject.env.DbParamsPostgres;

public class DbConfigPostgres implements DbConfig {

  private DbConfigPostgres() {}

  private static final DbConfigPostgres instance = new DbConfigPostgres();

  public static DbConfigPostgres instance() {
    return instance;
  }

  @Override
  public String url() {
    return DbParamsPostgres.url;
  }

  @Override
  public String username() {
    return DbParamsPostgres.username;
  }

  @Override
  public String password() {
    return DbParamsPostgres.password;
  }

  @Override
  public String dbName() {
    return DbParamsPostgres.dbName;
  }
}
