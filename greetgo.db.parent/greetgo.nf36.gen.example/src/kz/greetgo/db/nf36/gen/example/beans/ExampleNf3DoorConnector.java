package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.JdbcNf36UpserterPostgresAdapter;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.gen.example.generated.impl.AbstractExampleUpserter;
import kz.greetgo.db.nf36.model.SqlLog;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

@Bean
public class ExampleNf3DoorConnector extends AbstractExampleUpserter {

  public BeanGetter<Jdbc> jdbc;

  @Override
  protected Nf36Upserter createUpserter() {
    return new JdbcNf36UpserterPostgresAdapter(jdbc.get(), new SqlLogAcceptor() {
      @Override
      public boolean isTraceEnabled() {
        return true;
      }

      @Override
      public boolean isErrorEnabled() {
        return true;
      }

      @Override
      public void accept(SqlLog sqlLog) {
        System.out.println(sqlLog.toStr(true, true, true));
      }
    });
  }
}
