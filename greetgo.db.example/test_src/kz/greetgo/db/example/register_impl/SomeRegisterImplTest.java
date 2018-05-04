package kz.greetgo.db.example.register_impl;

import kz.greetgo.db.example.infrastructure.BeanConfigTests;
import kz.greetgo.db.example.register.SomeRegister;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import org.testng.annotations.Test;

@ContainerConfig(BeanConfigTests.class)
public class SomeRegisterImplTest extends AbstractDepinjectTestNg {

  public BeanGetter<SomeRegister> someRegister;

  @Test
  public void testName() throws Exception {
    someRegister.get().hello();
    System.out.println("asd");
  }
}