package nf36_example_with_depinject.util;

import java.math.BigDecimal;

public class CorrectionUtil {
  public static <T> T correctType(Object object) {
    if (object == null) {
      return null;
    }
    if (object instanceof BigDecimal) {
      //noinspection unchecked
      return (T) (Object) ((BigDecimal) object).longValue();
    }

    //noinspection unchecked
    return (T) object;
  }
}
