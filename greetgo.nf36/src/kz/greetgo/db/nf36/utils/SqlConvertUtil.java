package kz.greetgo.db.nf36.utils;

import kz.greetgo.db.nf36.errors.CannotConvertFromSql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class SqlConvertUtil {

  public static Object forSql(Object value) {
    if (value == null) {
      return null;
    }
    if (value.getClass().isEnum() || value instanceof Enum) {
      try {
        Method method = Enum.class.getMethod("name");
        return method.invoke(value);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
    if (value instanceof java.sql.Date) {
      return value;
    }
    if (value instanceof java.sql.Time) {
      return value;
    }
    if (value instanceof java.sql.Timestamp) {
      return value;
    }
    if (value instanceof java.util.Date) {
      java.util.Date date = (java.util.Date) value;
      return new java.sql.Timestamp(date.getTime());
    }
    if (value instanceof Boolean || value.getClass().equals(Boolean.TYPE)) {
      return (boolean) value ? 1 : 0;
    }
    return value;
  }

  public static Object fromSql(Object value) {
    return fromSql(value, null);
  }

  public static Long asLong(Object fieldValue) {
    if (fieldValue == null) {
      return null;
    }
    if (fieldValue instanceof Long) {
      return (Long) fieldValue;
    }
    if (fieldValue instanceof Integer) {
      return (long) (Integer) fieldValue;
    }
    if (fieldValue instanceof BigDecimal) {
      return ((BigDecimal) fieldValue).longValue();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromSql(Object value, Class<T> toType) {
    if (value == null) {
      if (toType == null) {
        return null;
      }
      if (toType.isPrimitive()) {
        return (T) defaultPrimitiveValue(toType);
      }
      return null;
    }
    if (value instanceof java.util.Date) {
      return (T) new java.util.Date(((java.util.Date) value).getTime());
    }

    if (toType != null) {
      return (T) sqlValueConvertTo(value, toType);
    }

    {
      Class<?> valueClass = value.getClass();
      if ("oracle.sql.TIMESTAMP".equals(valueClass.getName())) {
        try {
          Timestamp ts = (Timestamp) value.getClass().getMethod("timestampValue").invoke(value);
          return (T) new Date(ts.getTime());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    if ("oracle.sql.CLOB".equals(value.getClass().getName())
        || "oracle.sql.NCLOB".equals(value.getClass().getName())) {

      try {
        return (T) value.getClass().getMethod("stringValue").invoke(value);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }

    }

    return (T) value;
  }

  private static Object defaultPrimitiveValue(Class<?> pr) {
    if (Integer.TYPE.equals(pr)) {
      return 0;
    }
    if (Long.TYPE.equals(pr)) {
      return 0L;
    }
    if (Boolean.TYPE.equals(pr)) {
      return Boolean.FALSE;
    }
    if (Short.TYPE.equals(pr)) {
      return (short) 0;
    }
    if (Float.TYPE.equals(pr)) {
      return 0f;
    }
    if (Double.TYPE.equals(pr)) {
      return 0d;
    }
    if (Byte.TYPE.equals(pr)) {
      return (byte) 0;
    }
    if (Character.TYPE.equals(pr)) {
      return (char) 0;
    }

    throw new IllegalArgumentException("No default value for class " + pr);
  }

  private static Object sqlValueConvertTo(Object value, Class<?> toType) {
    if (toType == null) {
      throw new IllegalArgumentException("toType == null");
    }

    if (toType == Long.TYPE && value instanceof Long) {
      return value;
    }
    if (toType == Integer.TYPE && value instanceof Integer) {
      return value;
    }
    if (toType == Double.TYPE && value instanceof Double) {
      return value;
    }
    if (toType == Float.TYPE && value instanceof Float) {
      return value;
    }

    if (toType.isAssignableFrom(value.getClass())) {
      return value;
    }

    if (toType.isAssignableFrom(Long.class) || toType == Long.TYPE) {
      if (value instanceof Long) {
        return value;
      }
      if (value instanceof Integer) {
        return ((Integer) value).longValue();
      }
      if (value instanceof Float) {
        return ((Float) value).longValue();
      }
      if (value instanceof Double) {
        return ((Double) value).longValue();
      }
      if (value instanceof BigDecimal) {
        return ((BigDecimal) value).longValue();
      }

      throw new CannotConvertFromSql(value, toType);
    }

    if (toType.isAssignableFrom(Integer.class) || toType == Integer.TYPE) {
      if (value instanceof Integer) {
        return value;
      }
      if (value instanceof Long) {
        return ((Long) value).intValue();
      }
      if (value instanceof Float) {
        return ((Float) value).intValue();
      }
      if (value instanceof Double) {
        return ((Double) value).intValue();
      }
      if (value instanceof BigDecimal) {
        return ((BigDecimal) value).intValue();
      }

      throw new CannotConvertFromSql(value, toType);
    }

    if (toType.isAssignableFrom(Double.class) || toType == Double.TYPE) {
      if (value instanceof Integer) {
        return new Double((Integer) value);
      }
      if (value instanceof Long) {
        return new Double(((Long) value).toString());
      }
      if (value instanceof Float) {
        return new Double(value.toString());
      }
      if (value instanceof Double) {
        return value;
      }
      if (value instanceof BigDecimal) {
        return ((BigDecimal) value).doubleValue();
      }

      throw new CannotConvertFromSql(value, toType);
    }

    if (toType.isEnum()) {

      if (value instanceof String) {

        try {
          Method valueOf = toType.getMethod("valueOf", String.class);
          //noinspection JavaReflectionInvocation
          return valueOf.invoke(null, value);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

      }

      Integer i = asIntegerNoException(value);
      if (i != null) {

        try {
          Object[] vv = (Object[]) toType.getMethod("values").invoke(null);
          if (i < 0 || i >= vv.length) {
            throw new CannotConvertFromSql("Out of range: asd = "
                + value, value.getClass(), toType);
          }
          return vv[i];
        } catch (Exception e) {
          if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
          }
          throw new RuntimeException(e);
        }

      }

      throw new CannotConvertFromSql(value, toType);
    }

    if (String.class.equals(toType)) {
      if (value instanceof String) { return value; }
      if ("oracle.sql.CLOB".equals(value.getClass().getName()) || "oracle.sql.NCLOB".equals(value.getClass().getName())) {
        try {
          return value.getClass().getMethod("stringValue").invoke(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
      return String.valueOf(value);
    }

    if (Boolean.class.equals(toType) || Boolean.TYPE.equals(toType)) {
      if (value instanceof String) {

        switch (((String) value).trim().toLowerCase()) {
          case "0":
          case "":
          case "no":
          case "n":
          case "f":
          case "0.0":
          case "0.":
          case "false":
          case "off":
          case "нет":
          case "ложь":
          case "н":
          case "л":
          case "выкл":
            return false;
          default:
            return true;
        }

      }

      if (value instanceof Integer) {
        return 0 != (Integer) value;
      }
      if (value instanceof Long) {
        return 0L != (Long) value;
      }
      if (value instanceof Float) {
        return 0F != (Float) value;
      }
      if (value instanceof Double) {
        return 0.0 != (Double) value;
      }
      if (value instanceof BigDecimal) {
        return !BigDecimal.ZERO.equals(value);
      }

      throw new CannotConvertFromSql(value, toType);
    }

    //
    // BEGIN oracle.sql.TIMESTAMP
    //
    {
      Class<?> valueClass = value.getClass();
      if (ORACLE_TIMESTAMP.equals(valueClass.getName())) {
        try {
          Timestamp ts = (Timestamp) value.getClass().getMethod("timestampValue").invoke(value);
          return new Date(ts.getTime());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      if (ORACLE_TIMESTAMP_TZ.equals(valueClass.getName())) {
        try {
          throw new CannotConvertFromSql(

              "Type " + ORACLE_TIMESTAMP_TZ + " cannot be converted to java.util.Date without java.sql.Connection",

              Class.forName(ORACLE_TIMESTAMP_TZ), toType);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }

      }
    }
    //
    // END oracle.sql.TIMESTAMP
    //

    throw new CannotConvertFromSql(value, toType);
  }

  @SuppressWarnings("SpellCheckingInspection")
  private static final String ORACLE_TIMESTAMP_TZ = "oracle.sql.TIMESTAMPTZ";
  private static final String ORACLE_TIMESTAMP = "oracle.sql.TIMESTAMP";

  public static Integer asIntegerNoException(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Integer) {
      return (Integer) value;
    }
    if (value instanceof Long) {
      return ((Long) value).intValue();
    }
    if (value instanceof BigDecimal) {
      return ((BigDecimal) value).intValue();
    }
    return null;
  }

}