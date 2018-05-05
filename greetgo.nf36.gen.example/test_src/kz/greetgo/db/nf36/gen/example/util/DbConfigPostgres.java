package kz.greetgo.db.nf36.gen.example.util;

import kz.greetgo.db.nf36.gen.example.env.DbParamsPostgres;
import kz.greetgo.db.worker.DbConfig;

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
