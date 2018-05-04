package kz.greetgo.db.example.register_impl;

import kz.greetgo.db.example.register.SomeRegister;
import kz.greetgo.db.example.utils.TestParent;
import kz.greetgo.depinject.core.BeanGetter;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SomeRegisterImplTest extends TestParent {

  public BeanGetter<SomeRegister> someRegister;

  @Test
  public void add() throws Exception {
    int res = someRegister.get().add(2, 3);
    assertThat(res).isEqualTo(5);
  }
}