package kz.greetgo.db.worker.postgres;

import kz.greetgo.db.worker.db.DbParameters;
import kz.greetgo.depinject.core.Bean;

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
