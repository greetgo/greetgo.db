package nf36_example_with_depinject.tests;

import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.util.DbTypeSource;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.Test;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;
import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class JdbcSequenceNextAbstractAdapterPostgresTest extends ParentDbTests {

  public BeanGetter<DbTypeSource> dbTypeSource;
  public BeanGetter<SqlLogAcceptor> logAcceptor;

  @Test
  public void nextInt() {

    String sequenceName = "s_" + RND.str(10);
    int startWith = RND.plusInt(100_000);

    exec("create sequence " + sequenceName + " start with " + startWith);

    SequenceNext sequenceNext = newNf36Builder()
        .sequenceNext()
        .database(dbTypeSource.get().currentDbType())
        .setJdbc(jdbc.get())
        .setLogAcceptor(logAcceptor.get())
        .build();

    {
      //
      //
      int next = sequenceNext.nextInt(sequenceName);
      //
      //
      assertThat(next).isEqualTo(startWith);
    }
    {
      //
      //
      int next = sequenceNext.nextInt(sequenceName);
      //
      //
      assertThat(next).isEqualTo(startWith + 1);
    }
  }

  @Test
  public void nextLong() {

    String sequenceName = "s_" + RND.str(10);
    long startWith = RND.plusLong(100_000_000_000L);

    exec("create sequence " + sequenceName + " start with " + startWith);

    SequenceNext sequenceNext = newNf36Builder()
        .sequenceNext()
        .database(dbTypeSource.get().currentDbType())
        .setJdbc(jdbc.get())
        .setLogAcceptor(logAcceptor.get())
        .build();

    {
      //
      //
      long next = sequenceNext.nextLong(sequenceName);
      //
      //
      assertThat(next).isEqualTo(startWith);
    }
    {
      //
      //
      long next = sequenceNext.nextLong(sequenceName);
      //
      //
      assertThat(next).isEqualTo(startWith + 1);
    }
  }
}
