package kz.greetgo.db;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

class TransactionMediator implements MethodInterceptor {
  private final TransactionInvoker transactionInvoker;

  public TransactionMediator(TransactionManager transactionManager, Object wrappingObject) {
    transactionInvoker = new TransactionInvoker(transactionManager, wrappingObject);
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    return transactionInvoker.invoke(method, args);
  }
}
