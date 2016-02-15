package kz.greetgo.gbatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилиты для облегчения работы с рефлекцией
 * 
 * @author pompei
 */
public class ReflectUtil {
  
  /**
   * Кэширует первые результат, и возвращает его при последующих запросах
   * 
   * @param getter
   *          кэшируемый геттер
   * @return кэш геттера
   */
  public static <T> Getter<T> cachedFirst(final Getter<T> getter) {
    return new Getter<T>() {
      T returning = null;
      boolean first = true;
      
      @Override
      public T get() {
        if (first) {
          returning = getter.get();
          first = false;
        }
        return returning;
      }
    };
  }
  
  /**
   * Уменьшает регистр первого символа строки
   * 
   * @param str
   *          исходная строка
   * @return преобразованна строка
   */
  public static String firstToLowcase(String str) {
    if (str == null) return null;
    if (str.length() < 2) return str.toLowerCase();
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }
  
  /**
   * Сканирует класс объекта, и формирует список объектов, которые производят установку полей
   * объекта
   * 
   * @param object
   *          сканируемый объект
   * @return мапа со списком установщиков
   */
  public static Map<String, Setter> scanSetters(final Object object) {
    if (object == null) return new HashMap<>();
    
    abstract class LocalSetter implements Setter {
      abstract void setName(String name);
    }
    
    Map<String, LocalSetter> map = new HashMap<>();
    
    for (final Method method : object.getClass().getMethods()) {
      if (method.getName() == null) continue;
      if (method.getName().length() <= 3) continue;
      if (!"set".equals(method.getName().substring(0, 3))) continue;
      if (method.getParameterTypes().length != 1) continue;
      final String name = firstToLowcase(method.getName().substring(3));
      map.put(name, new LocalSetter() {
        String myName = name;
        
        @Override
        public Class<?> type() {
          return method.getParameterTypes()[0];
        }
        
        @Override
        void setName(String nameArg) {
          myName = nameArg;
        }
        
        @Override
        public String name() {
          return myName;
        }
        
        @Override
        public void set(Object value) {
          try {
            method.invoke(object, value);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }
        
      });
    }//FOR by methods
    
    for (final Field field : object.getClass().getFields()) {
      {
        LocalSetter setter = map.get(firstToLowcase(field.getName()));
        if (setter != null) {
          setter.setName(field.getName());
          continue;
        }
      }
      
      map.put(field.getName(), new LocalSetter() {
        @Override
        public Class<?> type() {
          return field.getType();
        }
        
        @Override
        public String name() {
          return field.getName();
        }
        
        @Override
        void setName(String name) {
          throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(Object value) {
          try {
            field.set(object, value);
          } catch (IllegalAccessException e1) {
            field.setAccessible(true);
            try {
              field.set(object, value);
            } catch (IllegalAccessException e) {
              throw new RuntimeException(e);
            }
          }
        }
      });
      
    }//FOR by fields
    
    {
      Map<String, Setter> ret = new HashMap<>();
      for (Setter setter : map.values()) {
        ret.put(setter.name(), setter);
      }
      return ret;
    }
  }
}
