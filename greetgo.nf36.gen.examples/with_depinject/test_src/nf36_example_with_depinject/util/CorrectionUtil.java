package nf36_example_with_depinject.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;

public class CorrectionUtil {
  public static <T> T correctTypeForJava(Object object, Connection con) {
    if (object == null) {
      return null;
    }
    if (object instanceof BigDecimal) {
      //noinspection unchecked
      return (T) (Object) ((BigDecimal) object).longValue();
    }

    if (object.getClass().getName().equals("oracle.sql.NCLOB")) {
      try {
        Method method = object.getClass().getMethod("stringValue");
        //noinspection unchecked
        return (T) method.invoke(object);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    //noinspection unchecked
    return (T) object;
  }
}
