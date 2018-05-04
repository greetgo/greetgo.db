package kz.greetgo.db.example.register_impl;

import kz.greetgo.db.InTransaction;
import kz.greetgo.db.example.register.SomeRegister;
import kz.greetgo.db.example.register_impl.jdbc.JdbcAdd;
import kz.greetgo.db.example.utils.JdbcExample;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;

@Bean
@InTransaction
public class SomeRegisterImpl implements SomeRegister {
  public BeanGetter<JdbcExample> jdbcExample;

  @Override
  public int add(int a, int b) {
    return jdbcExample.get().execute(new JdbcAdd(a, b));
  }
}
