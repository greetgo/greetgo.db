package kz.greetgo.gbatis.util.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassAcceptor {

  private Class<?> aClass;

  interface Getter {
    <T> T get(Object object);
  }

  interface Setter {
    void set(Object object, Object value);
  }

  private final Map<String, Getter> getterMap = new LinkedHashMap<>();
  private final Map<String, Setter> setterMap = new LinkedHashMap<>();

  public ClassAcceptor(Class<?> aClass) {
    this.aClass = aClass;

    appendForFields(aClass);

    appendForMethods(aClass);

  }

  public List<String> getReadAttributes() {
    List<String> ret = new ArrayList<>();
    ret.addAll(getterMap.keySet());
    return ret;
  }

  private void appendForFields(Class<?> aClass) {
    for (final Field field : aClass.getFields()) {
      getterMap.put(field.getName().toUpperCase(), new Getter() {
        @Override
        public <T> T get(Object object) {
          try {
            return castIt(field.get(object));
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      });
      setterMap.put(field.getName().toUpperCase(), new Setter() {
        @Override
        public void set(Object object, Object value) {
          try {
            field.set(object, value);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T castIt(Object o) {
    return (T) o;
  }

  private void appendForMethods(Class<?> aClass) {
    for (final Method method : aClass.getMethods()) {
      String methodName = method.getName();
      if (methodName.length() > 3 && methodName.startsWith("get") && method.getParameterTypes().length == 0) {
        String attributeName = methodName.substring(3).toUpperCase();
        getterMap.put(attributeName, new Getter() {
          @Override
          public <T> T get(Object object) {
            try {
              return castIt(method.invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new RuntimeException(e);
            }
          }
        });
        continue;
      }

      if (methodName.length() > 2 && methodName.startsWith("is") && method.getParameterTypes().length == 0
          && (method.getReturnType() == Boolean.class || method.getReturnType() == Boolean.TYPE)) {
        String attributeName = methodName.substring(2).toUpperCase();
        getterMap.put(attributeName, new Getter() {
          @Override
          public <T> T get(Object object) {
            try {
              return castIt(method.invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new RuntimeException(e);
            }
          }
        });
        continue;
      }

      if (methodName.length() > 3 && methodName.startsWith("set") && method.getParameterTypes().length == 1) {
        String attributeName = methodName.substring(3).toUpperCase();
        setterMap.put(attributeName, new Setter() {
          @Override
          public void set(Object object, Object value) {
            try {
              method.invoke(object, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new RuntimeException(e);
            }
          }
        });
      }

    }
  }

  public <T> T get(Object object, String attributeName) {
    Getter getter = getterMap.get(attributeName);
    if (getter == null) throw new RuntimeException("No getter for attribute " + attributeName + " in " + aClass);
    return getter.get(object);
  }

  public void set(Object object, String attributeName, Object value) {
    Setter setter = setterMap.get(attributeName);
    if (setter == null) throw new RuntimeException("No setter for attribute " + attributeName + " in " + aClass);
    setter.set(object, value);
  }
}
