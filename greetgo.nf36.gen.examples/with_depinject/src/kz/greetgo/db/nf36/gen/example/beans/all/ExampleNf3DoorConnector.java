package kz.greetgo.db.nf36.gen.example.beans.all;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.gen.example.generated.impl.AbstractExampleUpserter;
import kz.greetgo.db.nf36.gen.example.util.AuthorGetter;
import kz.greetgo.db.nf36.gen.example.util.DbTypeSource;
import kz.greetgo.db.nf36.gen.example.util.MySqlLogAcceptor;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;

@Bean
public class ExampleNf3DoorConnector extends AbstractExampleUpserter {

  public BeanGetter<Jdbc> jdbc;
  public BeanGetter<DbTypeSource> dbTypeSource;
  public BeanGetter<AuthorGetter> authorGetter;

  private final SqlLogAcceptor logAcceptor = new MySqlLogAcceptor();

  @Override
  protected Nf36Upserter createUpserter() {
    return newNf36Builder()
      .upserter()
      .database(dbTypeSource.get().currentDbType())
      .setJdbc(jdbc.get())
      .setLogAcceptor(logAcceptor)
      .build()
      .setAuthor(authorGetter.get().getAuthor())
      ;
  }

}
