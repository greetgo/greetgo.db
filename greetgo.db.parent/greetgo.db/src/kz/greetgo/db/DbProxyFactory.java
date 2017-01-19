package kz.greetgo.db;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DbProxyFactory {

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

  public final Object createProxyFor(Object wrappingObject, Class<?>... interfaces) {
    if (interfaces.length == 0) throw new IllegalArgumentException("No interfaces");
    final TransactionInvoker transactionInvoker = new TransactionInvoker(transactionManager, wrappingObject);
    //noinspection unchecked
    return Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return transactionInvoker.invoke(method, args);
      }
    });
  }

}
