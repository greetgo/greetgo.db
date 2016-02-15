package kz.greetgo.db;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;

public class DbProxyFactory {

  static class Hello {}

  private final TransactionManager transactionManager;

  public DbProxyFactory(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public Object createProxyFor(Object object) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(object.getClass());
    enhancer.setCallback(new TransactionMediator(transactionManager, object));
    return enhancer.create();
  }

}
