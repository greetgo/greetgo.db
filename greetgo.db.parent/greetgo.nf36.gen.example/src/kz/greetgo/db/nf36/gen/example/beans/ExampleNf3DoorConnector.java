package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.JdbcNf36UpserterPostgresAdapter;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.db.nf36.gen.example.generated.impl.AbstractExampleUpserter;
import kz.greetgo.db.nf36.gen.example.util.AuthorGetter;
import kz.greetgo.db.nf36.gen.example.util.MySqlLogAcceptor;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

@Bean
public class ExampleNf3DoorConnector extends AbstractExampleUpserter {

  public BeanGetter<Jdbc> jdbc;

  private final SqlLogAcceptor logAcceptor = new MySqlLogAcceptor();

  public BeanGetter<AuthorGetter> authorGetter;

  @Override
  protected Nf36Upserter createUpserter() {
    return new JdbcNf36UpserterPostgresAdapter(jdbc.get(), logAcceptor)
      .setAuthor(authorGetter.get().getAuthor())
      ;
  }

}
