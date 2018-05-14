package nf36_example_with_depinject.beans.oracle;

import kz.greetgo.db.DbType;
import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.util.DbTypeSource;

@Bean
public class DbTypeSourceOracle implements DbTypeSource {
  @Override
  public DbType currentDbType() {
    return DbType.Oracle;
  }
}
