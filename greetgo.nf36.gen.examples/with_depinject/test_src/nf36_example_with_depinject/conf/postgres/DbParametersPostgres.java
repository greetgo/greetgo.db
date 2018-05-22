package nf36_example_with_depinject.conf.postgres;

import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.conf.DbParameters;

@Bean
public class DbParametersPostgres implements DbParameters {

  public String schema() {
    return "memory_never_be_superfluous";
  }

  @Override
  public String nf6prefix() {
    return schema() + ".";
  }

  @Override
  public String baseSubPackage() {
    return "postgres";
  }

  @Override
  public String mainClassesSuffix() {
    return "Postgres";
  }
}
