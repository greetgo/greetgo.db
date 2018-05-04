package kz.greetgo.db.example.configs;

import kz.greetgo.db.worker.DbConfig;

public class PostgresDbConfig implements DbConfig {

  private PostgresDbConfig() {}

  private static final PostgresDbConfig instance = new PostgresDbConfig();

  public static DbConfig instance() {
    return instance;
  }

  @Override
  public String url() {
    return PostgresConfig.url();
  }

  @Override
  public String username() {
    return PostgresConfig.username();
  }

  @Override
  public String password() {
    return PostgresConfig.password();
  }

  @Override
  public String dbName() {
    return PostgresConfig.dbName();
  }
}
