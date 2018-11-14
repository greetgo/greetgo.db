package kz.greetgo.db.worker.oracle;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.db.worker.db.DbParameters;

@Bean
public class DbParametersOracle implements DbParameters {
  @Override
  public String nf6prefix() {
    return "m_";
  }

  @Override
  public String baseSubPackage() {
    return "oracle";
  }

  @Override
  public String mainClassesSuffix() {
    return "Oracle";
  }
}
