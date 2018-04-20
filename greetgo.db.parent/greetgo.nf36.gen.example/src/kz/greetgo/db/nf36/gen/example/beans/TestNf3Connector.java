package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.impl.AbstractTestNf3Door;
import kz.greetgo.db.nf36.gen.example.util.JdbcNf36UpserterAdapter;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

@Bean
public class TestNf3Connector extends AbstractTestNf3Door {

  public BeanGetter<Jdbc> jdbc;

  @Override
  protected Nf36Upserter createUpserter() {
    return new JdbcNf36UpserterAdapter(jdbc.get());
  }
}
