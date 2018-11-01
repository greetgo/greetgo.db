package kz.greetgo.db.nf36.bridges;

import java.util.concurrent.ConcurrentHashMap;

public class ClassAccessorStorage {

  private final ConcurrentHashMap<Class<?>, ClassAccessor> storage = new ConcurrentHashMap<>();

  public ClassAccessor get(Class<?> aClass) {
    return storage.computeIfAbsent(aClass, this::create);
  }

  private ClassAccessor create(Class<?> aClass) {
    return new ClassAccessor(aClass);
  }
}
