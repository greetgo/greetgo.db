package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.errors.CannotExtractFieldFromClass;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static kz.greetgo.db.nf36.utils.UtilsNf36.javaNameToDbName;

public class ClassAccessor {

  private final Map<String, Function<Object, Object>> getterMap = new HashMap<>();
  private final Class<?> accessingClass;

  public ClassAccessor(Class<?> accessingClass) {
    this.accessingClass = accessingClass;

    for (Field field : accessingClass.getFields()) {
      getterMap.put(field.getName(), object -> {
        try {
          return field.get(object);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      });
    }

    for (Method method : accessingClass.getMethods()) {
      if (method.getParameterTypes().length > 0) {
        continue;
      }

      String fieldName = extractGetterFieldName(method.getName());
      if (fieldName == null) {
        continue;
      }

      getterMap.put(fieldName, object -> {
        try {
          return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      });
    }

    for (Field field : accessingClass.getFields()) {

      String dbName = javaNameToDbName(field.getName());

      if (getterMap.containsKey(dbName)) {
        continue;
      }

      getterMap.put(dbName, object -> {
        try {
          return field.get(object);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      });
    }

    for (Method method : accessingClass.getMethods()) {
      if (method.getParameterTypes().length > 0) {
        continue;
      }

      String fieldName = extractGetterFieldName(method.getName());
      if (fieldName == null) {
        continue;
      }

      String dbName = javaNameToDbName(fieldName);

      if (getterMap.containsKey(dbName)) {
        continue;
      }

      getterMap.put(dbName, object -> {
        try {
          return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      });
    }

  }

  static String extractGetterFieldName(String name) {

    for (String pre : new String[]{"is", "get"}) {
      if (name.startsWith(pre)) {

        String name2 = name.substring(pre.length());
        if (name2.length() == 0) {
          return null;
        }

        String first = name2.substring(0, 1);
        String firstLower = first.toLowerCase();

        if (first.equals(firstLower)) {
          return null;
        }

        return firstLower + name2.substring(1);
      }
    }

    return null;
  }

  public boolean isAbsent(String fieldName) {
    return !getterMap.containsKey(fieldName);
  }

  public Object extractValue(String fieldName, Object object) {
    Function<Object, Object> extractor = getterMap.get(fieldName);

    if (extractor == null) {
      throw new CannotExtractFieldFromClass(fieldName, accessingClass);
    }

    return extractor.apply(object);
  }

  public void setValue(Object destinationObject, String fieldName, Object value) {
    throw new NotImplementedException();
  }

  public boolean hasSetter(String fieldName) {
    throw new NotImplementedException();
  }

  public Class<?> setterType(String fieldName) {
    throw new NotImplementedException();
  }
}
