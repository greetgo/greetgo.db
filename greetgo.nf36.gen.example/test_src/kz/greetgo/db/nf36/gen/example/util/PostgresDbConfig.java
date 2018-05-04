package kz.greetgo.db.nf36.gen.example.util;

import kz.greetgo.db.nf36.gen.example.env.DbParams;
import kz.greetgo.db.worker.DbConfig;

public class PostgresDbConfig implements DbConfig {

  private PostgresDbConfig() {}

  private static final PostgresDbConfig instance = new PostgresDbConfig();

  public static PostgresDbConfig instance() {
    return instance;
  }

  @Override
  public String url() {
    return DbParams.url;
  }

  @Override
  public String username() {
    return DbParams.username;
  }

  @Override
  public String password() {
    return DbParams.password;
  }

  @Override
  public String dbName() {
    return DbParams.dbName;
  }
}
