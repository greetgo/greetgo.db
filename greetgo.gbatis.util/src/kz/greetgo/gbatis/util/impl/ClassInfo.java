package kz.greetgo.gbatis.util.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassInfo {

  private final Map<Class<?>, ClassAcceptor> cache = new ConcurrentHashMap<>();

  public ClassAcceptor getClassAcceptor(Class<?> aClass) {
    {
      ClassAcceptor classAcceptor = cache.get(aClass);
      if (classAcceptor != null) return classAcceptor;
    }
    {
      ClassAcceptor classAcceptor = new ClassAcceptor(aClass);
      cache.put(aClass, classAcceptor);
      return classAcceptor;
    }
  }
}
