package kz.greetgo.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Creates proxy for automatic open and commit transactions.
 */
public class DbProxyFactory {

  private final TransactionManager transactionManager;

  public DbProxyFactory(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  /**
   * Use {@link #createProxyFor(Object, Class[])}
   *
   * @param object it is wrappingObject of {@link #createProxyFor(Object, Class[])}
   * @return it is what returns {@link #createProxyFor(Object, Class[])}
   */
  @Deprecated
  public Object createProxyFor(Object object) {
    TransactionInvoker transactionInvoker = new TransactionInvoker(transactionManager, object);

    try {
      Class<?> enhancerClass = Class.forName("net.sf.cglib.proxy.Enhancer");
      Class<?> callbackClass = Class.forName("net.sf.cglib.proxy.Callback");
      Class<?> methodInterceptorClass = Class.forName("net.sf.cglib.proxy.MethodInterceptor");

      Method setSuperclass = enhancerClass.getMethod("setSuperclass", Class.class);
      Method setCallback = enhancerClass.getMethod("setCallback", callbackClass);
      Method create = enhancerClass.getMethod("create");

      Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(),
        new Class<?>[]{methodInterceptorClass},
        (proxyArg, method, args) -> {
          if ("intercept".equals(method.getName()) && method.getParameterTypes().length == 4) {
            return transactionInvoker.invoke((Method) args[1], (Object[]) args[2]);
          }
          throw new IllegalAccessException("Called " + method.toGenericString());
        });

      Object enhancerInstance = enhancerClass.newInstance();

      setSuperclass.invoke(enhancerInstance, object.getClass());
      //noinspection JavaReflectionInvocation
      setCallback.invoke(enhancerInstance, proxy);

      return create.invoke(enhancerInstance);

    } catch (ClassNotFoundException e) {
      throw new NoCGLIB(e);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates proxy for wrappingObject, where to prepare correct transaction functionality
   *
   * @param wrappingObject proxy creates for it
   * @param interfaces     return object must implements these interfaces
   * @return proxy for wrappingObject
   */
  public final Object createProxyFor(Object wrappingObject, Class<?>... interfaces) {
    if (interfaces.length == 0) throw new IllegalArgumentException("No interfaces");
    final TransactionInvoker transactionInvoker = new TransactionInvoker(transactionManager, wrappingObject);

    //noinspection unchecked
    return Proxy.newProxyInstance(
      getClass().getClassLoader(),
      interfaces,
      (proxy, method, args) -> transactionInvoker.invoke(method, args)
    );
  }

  public final <T> T createProxyOn(Class<T> anInterface, Object wrappingObject) {
    //noinspection unchecked
    return (T) createProxyFor(wrappingObject, anInterface);
  }
}
