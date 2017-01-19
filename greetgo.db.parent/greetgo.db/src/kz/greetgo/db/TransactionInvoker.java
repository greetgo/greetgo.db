package kz.greetgo.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionInvoker {
  private final TransactionManager transactionManager;
  private final Object wrappingObject;

  private final CallMeta classCallMeta;

  public TransactionInvoker(TransactionManager transactionManager, Object wrappingObject) {
    this.transactionManager = transactionManager;
    this.wrappingObject = wrappingObject;

    classCallMeta = createCallMetaOn(null, wrappingObject.getClass().getAnnotations());
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

  public Object invoke(Method method, Object[] args) throws Throwable {
    final CallMeta callMeta = getCallMeta(method);

    transactionManager.upLevel(callMeta);

    try {

      final Object ret = method.invoke(wrappingObject, args);
      transactionManager.downLevel(null);
      return ret;

    } catch (Throwable e) {

      e = prepare(e);

      transactionManager.downLevel(e);

      //noinspection ConstantConditions
      throw e;

    }
  }

  private static Throwable prepare(Throwable e) {
    if (e instanceof InvocationTargetException) return e.getCause();
    return e;
  }

  private final Map<Method, CallMeta> callMetaCache = new ConcurrentHashMap<>();

  private CallMeta getCallMeta(Method method) throws NoSuchMethodException {
    {
      final CallMeta ret = callMetaCache.get(method);
      if (ret != null) return ret;
    }

    {
      final CallMeta ret = extractCallMeta(method);
      callMetaCache.put(method, ret);
      return ret;
    }
  }

  private CallMeta extractCallMeta(Method method) throws NoSuchMethodException {
    CallMeta callMeta = createCallMetaOn(classCallMeta, method.getAnnotations());

    Method method2 = wrappingObject.getClass().getMethod(method.getName(), method.getParameterTypes());
    if (method2.equals(method)) return callMeta;

    return createCallMetaOn(callMeta, method2.getAnnotations());
  }
}
