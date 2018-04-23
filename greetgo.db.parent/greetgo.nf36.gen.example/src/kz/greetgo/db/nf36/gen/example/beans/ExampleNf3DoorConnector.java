package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.JdbcNf36UpserterPostgresAdapter;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.impl.AbstractExampleNf3Door;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

@Bean
public class ExampleNf3DoorConnector extends AbstractExampleNf3Door {

  public BeanGetter<Jdbc> jdbc;

  @Override
  protected Nf36Upserter createUpserter() {
    return new JdbcNf36UpserterPostgresAdapter(jdbc.get());
  }
}
