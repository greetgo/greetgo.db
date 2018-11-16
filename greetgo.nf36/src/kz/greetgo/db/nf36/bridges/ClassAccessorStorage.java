package kz.greetgo.db.nf36.bridges;

import java.util.concurrent.ConcurrentHashMap;

public class ClassAccessorStorage {

  private ClassAccessorStorage() {}

  private enum InstanceHolder {
    INSTANCE_HOLDER;
    final ClassAccessorStorage instance = new ClassAccessorStorage();
  }

  public static ClassAccessorStorage classAccessorStorage() {
    return InstanceHolder.INSTANCE_HOLDER.instance;
  }

  private final ConcurrentHashMap<Class<?>, ClassAccessor> storage = new ConcurrentHashMap<>();

  public ClassAccessor get(Class<?> aClass) {
    return storage.computeIfAbsent(aClass, this::create);
  }

  private ClassAccessor create(Class<?> aClass) {
    return new ClassAccessor(aClass);
  }
}
