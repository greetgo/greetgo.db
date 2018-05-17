package nf36_example_with_depinject.beans.all;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import nf36_example_with_depinject.generated.impl.AbstractExampleUpserter;
import nf36_example_with_depinject.util.AuthorGetter;
import nf36_example_with_depinject.util.DbTypeSource;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;

@Bean
public class ExampleUpserterConnector extends AbstractExampleUpserter {

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
      .build()
      .setAuthor(authorGetter.get().getAuthor())
      ;
  }

}
