package nf36_example_with_depinject.conf.oracle;

import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.conf.DbParameters;

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
