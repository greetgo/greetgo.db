package kz.greetgo.db.nf36.gen.example.beans.oracle;

import kz.greetgo.db.DbType;
import kz.greetgo.db.nf36.gen.example.util.DbTypeSource;
import kz.greetgo.depinject.core.Bean;

@Bean
public class DbTypeSourceOracle implements DbTypeSource {
  @Override
  public DbType currentDbType() {
    return DbType.Oracle;
  }
}
