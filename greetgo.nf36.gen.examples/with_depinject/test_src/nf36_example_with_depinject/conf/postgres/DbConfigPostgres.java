package nf36_example_with_depinject.conf.postgres;

import kz.greetgo.db.worker.DbConfig;
import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.env.DbParamsPostgres;

@Bean
public class DbConfigPostgres implements DbConfig {

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
