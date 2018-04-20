package kz.greetgo.db.nf36.utils;


import kz.greetgo.db.nf36.errors.CannotConvertFromSql;
import kz.greetgo.db.nf36.model.DbType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class SqlUtil {

  public static Object forSql(Object value) {
    if (value == null) return null;
    if (value.getClass().isEnum() || value instanceof Enum) {
      try {
        Method method = Enum.class.getMethod("name");
        return method.invoke(value);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
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
    if (fieldValue == null) return null;
    if (fieldValue instanceof Long) return (Long) fieldValue;
    if (fieldValue instanceof Integer) return (long) (Integer) fieldValue;
    if (fieldValue instanceof BigDecimal) return ((BigDecimal) fieldValue).longValue();
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromSql(Object value, Class<T> toType) {
    if (value == null) {
      if (toType == null) return null;
      if (toType.isPrimitive()) return (T) defaultPrimitiveValue(toType);
      return null;
    }
    if (value instanceof java.util.Date) {
      return (T) new java.util.Date(((java.util.Date) value).getTime());
    }

    if (toType != null) return (T) sqlValueConvertTo(value, toType);

    return (T) value;
  }

  private static Object defaultPrimitiveValue(Class<?> pr) {
    if (Integer.TYPE.equals(pr)) return 0;
    if (Long.TYPE.equals(pr)) return 0L;
    if (Boolean.TYPE.equals(pr)) return Boolean.FALSE;
    if (Short.TYPE.equals(pr)) return (short) 0;
    if (Float.TYPE.equals(pr)) return 0f;
    if (Double.TYPE.equals(pr)) return 0d;
    if (Byte.TYPE.equals(pr)) return (byte) 0;
    if (Character.TYPE.equals(pr)) return (char) 0;

    throw new IllegalArgumentException("No default value for class " + pr);
  }

  private static Object sqlValueConvertTo(Object value, Class<?> toType) {
    if (toType == null) throw new IllegalArgumentException("toType == null");

    if (toType == Long.TYPE && value instanceof Long) return value;
    if (toType == Integer.TYPE && value instanceof Integer) return value;
    if (toType == Double.TYPE && value instanceof Double) return value;
    if (toType == Float.TYPE && value instanceof Float) return value;

    if (toType.isAssignableFrom(value.getClass())) return value;

    if (toType.isAssignableFrom(Long.class) || toType == Long.TYPE) {
      if (value instanceof Long) return value;
      if (value instanceof Integer) return ((Integer) value).longValue();
      if (value instanceof Float) return ((Float) value).longValue();
      if (value instanceof Double) return ((Double) value).longValue();
      if (value instanceof BigDecimal) return ((BigDecimal) value).longValue();

      throw new CannotConvertFromSql(value, toType);
    }

    if (toType.isAssignableFrom(Integer.class) || toType == Integer.TYPE) {
      if (value instanceof Integer) return value;
      if (value instanceof Long) return ((Long) value).intValue();
      if (value instanceof Float) return ((Float) value).intValue();
      if (value instanceof Double) return ((Double) value).intValue();
      if (value instanceof BigDecimal) return ((BigDecimal) value).intValue();

      throw new CannotConvertFromSql(value, toType);
    }

    if (toType.isAssignableFrom(Double.class) || toType == Double.TYPE) {
      if (value instanceof Integer) return new Double((Integer) value);
      if (value instanceof Long) return new Double(((Long) value).toString());
      if (value instanceof Float) return new Double(value.toString());
      if (value instanceof Double) return value;
      if (value instanceof BigDecimal) return ((BigDecimal) value).doubleValue();

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
          if (i < 0 || i >= vv.length) throw new CannotConvertFromSql("Out of range: asd = "
            + value, value.getClass(), toType);
          return vv[i];
        } catch (Exception e) {
          if (e instanceof RuntimeException) throw (RuntimeException) e;
          throw new RuntimeException(e);
        }

      }

      throw new CannotConvertFromSql(value, toType);
    }

    if (String.class.equals(toType)) {
      if (value instanceof String) return value;
      if ("oracle.sql.CLOB".equals(value.getClass().getName())) {
        try {
          Method m = value.getClass().getMethod("stringValue");
          return m.invoke(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
      return String.valueOf(value);
    }

    if (Boolean.class.equals(toType) || Boolean.TYPE.equals(toType)) {
      if (value instanceof String) {
        String s = (String) value;
        s = s.trim().toLowerCase();

        if ("0".equals(s)) return false;
        if ("".equals(s)) return false;
        if ("no".equals(s)) return false;
        if ("n".equals(s)) return false;
        if ("f".equals(s)) return false;
        if ("0.0".equals(s)) return false;
        if ("0.".equals(s)) return false;
        if ("false".equals(s)) return false;
        if ("off".equals(s)) return false;
        if ("нет".equals(s)) return false;
        if ("ложь".equals(s)) return false;
        if ("н".equals(s)) return false;
        if ("л".equals(s)) return false;
        if ("выкл".equals(s)) return false;

        return true;
      }

      if (value instanceof Integer) return 0 != (Integer) value;
      if (value instanceof Long) return 0L != (Long) value;
      if (value instanceof Float) return 0F != (Float) value;
      if (value instanceof Double) return 0.0 != (Double) value;
      if (value instanceof BigDecimal) return !BigDecimal.ZERO.equals(value);

      throw new CannotConvertFromSql(value, toType);
    }

    //
    // BEGIN oracle.sql.TIMESTAMP
    //
    {
      Class<?> classs = null;
      try {
        classs = Class.forName("oracle.sql.TIMESTAMP");
      } catch (ClassNotFoundException ignore) {}

      if (classs != null && classs.equals(value.getClass())) {

        if (Date.class == toType) {
          try {
            Timestamp ts = (Timestamp) classs.getMethod("timestampValue").invoke(value);
            return new Date(ts.getTime());
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }

        throw new CannotConvertFromSql(value, toType);
      }
    }
    //
    // END oracle.sql.TIMESTAMP
    //

    throw new CannotConvertFromSql(value, toType);
  }

  public static Integer asIntegerNoException(Object value) {
    if (value == null) return null;
    if (value instanceof Integer) return (Integer) value;
    if (value instanceof Long) return ((Long) value).intValue();
    if (value instanceof BigDecimal) return ((BigDecimal) value).intValue();
    return null;
  }

  public static DbType convertType(Class<?> javaClass) {
    throw new NotImplementedException();
  }
}