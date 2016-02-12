package kz.greetgo.db;

import java.lang.reflect.Proxy;

public class ProxyFactory {

  private final TransactionManager transactionManager;

  public ProxyFactory(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public Object createProxyFor(Object object) {
    if (!(object instanceof HasMethodsInTransaction)) return object;

    return Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[]{object.getClass()},
      new TransactionMediator(transactionManager, object)
    );
  }

}
