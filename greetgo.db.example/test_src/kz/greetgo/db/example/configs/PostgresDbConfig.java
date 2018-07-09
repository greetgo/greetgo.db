package kz.greetgo.db.example.configs;

import kz.greetgo.db.example.utils.ExampleUtil;
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

  private static String extractDbName(String url) {
    final int i = url.lastIndexOf('/');
    if (i < 0) throw new RuntimeException("Left url = " + url);
    return url.substring(i + 1);
  }

  public static void main(String[] args) throws Exception {
    final DbConfig dbConfig = instance();
    ExampleUtil.recreateDb(dbConfig.url(), dbConfig.username(), dbConfig.password());
  }
}
