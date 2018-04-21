package kz.greetgo.db.nf36.utils;

import kz.greetgo.db.nf36.core.Nf3DefaultNow;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Long;
import kz.greetgo.db.nf36.core.Nf3NotNull;
import kz.greetgo.db.nf36.core.Nf3Short;
import kz.greetgo.db.nf36.core.Nf3Text;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.SqlType;

import java.lang.reflect.Field;
import java.util.Date;

public class SqlTypeUtil {

  private static class DbTypeImpl implements DbType {
    private SqlType sqlType;
    private int len;
    private boolean nullable;

    public DbTypeImpl(SqlType sqlType, int len, boolean nullable) {
      this.sqlType = sqlType;
      this.len = len;
      this.nullable = nullable;
    }

    @Override
    public SqlType sqlType() {
      return sqlType;
    }

    @Override
    public int len() {
      return len;
    }

    @Override
    public boolean nullable() {
      return nullable;
    }

    public boolean defaultNow = false;

    @Override
    public boolean defaultNow() {
      return defaultNow;
    }
  }

  public static DbType extractDbType(Field field, int enumLength) {
    if (String.class.equals(field.getType())) {
      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;
      if (field.getAnnotation(Nf3Text.class) != null) {
        return new DbTypeImpl(SqlType.CLOB, 0, nullable);
      }
      if (field.getAnnotation(Nf3Short.class) != null) {
        return new DbTypeImpl(SqlType.VARCHAR, 50, nullable);
      }
      if (field.getAnnotation(Nf3Long.class) != null) {
        return new DbTypeImpl(SqlType.VARCHAR, 2000, nullable);
      }

      boolean id = field.getAnnotation(Nf3ID.class) != null;

      return new DbTypeImpl(SqlType.VARCHAR, id ? 30 : 300, !id && nullable);
    }

    if (Integer.class.equals(field.getType())) return new DbTypeImpl(SqlType.INT, intLen(field), true);
    if (int.class.equals(field.getType())) return new DbTypeImpl(SqlType.INT, intLen(field), false);
    if (Long.class.equals(field.getType())) return new DbTypeImpl(SqlType.BIGINT, 0, true);
    if (long.class.equals(field.getType())) return new DbTypeImpl(SqlType.BIGINT, 0, false);
    if (Boolean.class.equals(field.getType())) return new DbTypeImpl(SqlType.BOOL, 0, true);
    if (boolean.class.equals(field.getType())) return new DbTypeImpl(SqlType.BOOL, 0, false);

    if (Date.class.equals(field.getType())) {
      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;
      DbTypeImpl ret = new DbTypeImpl(SqlType.TIMESTAMP, 0, nullable);
      ret.defaultNow = field.getAnnotation(Nf3DefaultNow.class) != null;
      return ret;
    }

    if (Enum.class.isAssignableFrom(field.getType())) {
      if (enumLength < 10) throw new RuntimeException("enumLength must be >= 10");
      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;
      return new DbTypeImpl(SqlType.VARCHAR, enumLength, nullable);
    }

    throw new RuntimeException("Cannot extract DbType from " + field);
  }

  private static int intLen(Field field) {
    return field.getAnnotation(Nf3Short.class) == null ? 4 : 8;
  }
}
