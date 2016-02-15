package kz.greetgo.db;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TransactionMediator implements MethodInterceptor {
  private final TransactionManager transactionManager;
  private final Object object;

  private final CallMeta classCallMeta;

  public TransactionMediator(TransactionManager transactionManager, Object object) {
    this.transactionManager = transactionManager;
    this.object = object;

    classCallMeta = createCallMetaOn(null, object.getClass().getAnnotations());
  }

  private static CallMeta createCallMetaOn(CallMeta parent, Annotation[] annotations) {
    IsolationLevel isolationLevel = null;
    Class<? extends Throwable>[] commitExceptions = null;

    for (Annotation annotation : annotations) {
      if (annotation instanceof InTransaction) {
        InTransaction inTransaction = (InTransaction) annotation;
        isolationLevel = inTransaction.value();
        continue;
      }
      if (annotation instanceof CommitOn) {
        CommitOn commitOn = (CommitOn) annotation;
        commitExceptions = commitOn.value();
        //noinspection UnnecessaryContinue
        continue;
      }
    }

    return new CallMeta(parent, isolationLevel, commitExceptions);
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

    final CallMeta callMeta = getCallMeta(method);

    transactionManager.upLevel(callMeta);

    try {

      final Object ret = method.invoke(object, args);
      transactionManager.downLevel(null);
      return ret;

    } catch (Throwable e) {

      e = prepare(e);

      transactionManager.downLevel(e);

      throw e;

    }

  }

  private static Throwable prepare(Throwable e) {
    if (e instanceof InvocationTargetException) {
      return e.getCause();
    }
    return e;
  }

  private final Map<Method, CallMeta> callMetaCache = new ConcurrentHashMap<>();

  private CallMeta getCallMeta(Method method) {
    {
      final CallMeta ret = callMetaCache.get(method);
      if (ret != null) return ret;
    }

    {
      final Annotation[] annotations = method.getAnnotations();
      final CallMeta ret = createCallMetaOn(classCallMeta, annotations);
      callMetaCache.put(method, ret);
      return ret;
    }
  }
}
