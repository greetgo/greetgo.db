package kz.greetgo.db;

import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DbProxyFactoryTest {

  public static class SomeClass {
    public int helloReturn;

    public int hello() {
      return helloReturn;
    }
  }

  @Test
  public void createProxyFor() throws Exception {
    TestExceptionCatcher exceptionCatcher = new TestExceptionCatcher();

    GreetgoTransactionManager transactionManager = new GreetgoTransactionManager();
    transactionManager.setExceptionCatcher(exceptionCatcher);

    DbProxyFactory pf = new DbProxyFactory(transactionManager);

    int helloReturn = RND.plusInt(1_000_000_000);

    SomeClass object = new SomeClass();
    object.helloReturn = helloReturn;

    SomeClass proxy = (SomeClass) pf.createProxyFor(object);

    int actualHelloReturn = proxy.hello();
    assertThat(actualHelloReturn).isEqualTo(helloReturn);

  }
}