package nf36_example_with_depinject.beans.oracle;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import nf36_example_with_depinject.beans.all.UpserterCreator;
import nf36_example_with_depinject.generated.impl.oracle.AbstractExampleUpserterOracle;
import nf36_example_with_depinject.util.AuthorGetter;
import nf36_example_with_depinject.util.DbTypeSource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;

@Bean
public class ExampleUpserterOracleConnector extends AbstractExampleUpserterOracle {

  public BeanGetter<Jdbc> jdbc;
  public BeanGetter<DbTypeSource> dbTypeSource;
  public BeanGetter<SqlLogAcceptor> logAcceptor;

  public BeanGetter<AuthorGetter> authorGetter;

  @Override
  protected Nf36Upserter createUpserter() {
    return newNf36Builder()
        .upserter()
        .database(dbTypeSource.get().currentDbType())
        .setJdbc(jdbc.get())
        .setLogAcceptor(logAcceptor.get())
        .setAuthor(authorGetter.get().getAuthor())
        .build()
        ;
  }

  @Bean
  public UpserterCreator createUpserterCreator() {
    return this::createUpserter;
  }

  @Override
  protected SequenceNext getSequenceNext() {
    return newNf36Builder()
        .sequenceNext()
        .setLogAcceptor(logAcceptor.get())
        .setJdbc(jdbc.get())
        .database(dbTypeSource.get().currentDbType())
        .build();
  }

  @Override
  protected Nf36Saver createSaver() {
    throw new NotImplementedException();
  }
}
