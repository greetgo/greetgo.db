package nf36_example_with_depinject.tests;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.beans.postgres.BeanConfigPostgres;
import nf36_example_with_depinject.util.DbTypeSource;
import org.testng.annotations.Test;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;

@ContainerConfig(BeanConfigPostgres.class)
public class JdbcNf36WhereUpdaterAdapterPostgresTests extends AbstractDepinjectTestNg {

  public BeanGetter<Jdbc> jdbc;
  public BeanGetter<DbTypeSource> dbTypeSource;
  public BeanGetter<SqlLogAcceptor> logAcceptor;

  @Test
  public void testName() {
    System.out.println(jdbc.get());

    Nf36WhereUpdater whereUpdater = newNf36Builder()
      .whereUpdater()
      .database(dbTypeSource.get().currentDbType())
      .setJdbc(jdbc.get())
      .setLogAcceptor(logAcceptor.get())
      .build()
      .setAuthor("Сталина на вас нет");

    System.out.println(whereUpdater);
  }
}
