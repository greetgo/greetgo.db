package nf36_example_with_depinject.conf.oracle;

import kz.greetgo.db.worker.DbConfig;
import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.env.DbParamsOracle;

@Bean
public class DbConfigOracle implements DbConfig {

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
