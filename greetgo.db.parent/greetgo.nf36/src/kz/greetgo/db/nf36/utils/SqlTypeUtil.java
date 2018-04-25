package kz.greetgo.db.nf36.utils;

import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Length;
import kz.greetgo.db.nf36.core.Nf3Long;
import kz.greetgo.db.nf36.core.Nf3NotNull;
import kz.greetgo.db.nf36.core.Nf3ReferenceTo;
import kz.greetgo.db.nf36.core.Nf3Scale;
import kz.greetgo.db.nf36.core.Nf3Short;
import kz.greetgo.db.nf36.core.Nf3Text;
import kz.greetgo.db.nf36.errors.ConflictError;
import kz.greetgo.db.nf36.model.DbType;
import kz.greetgo.db.nf36.model.SqlType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class SqlTypeUtil {

  private static class DbTypeImpl implements DbType {
    private SqlType sqlType;
    private int len;
    private boolean nullable;
    public int scale;

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
    public int scale() {
      return scale;
    }

    @Override
    public boolean nullable() {
      return nullable;
    }
  }

  public static DbType extractDbType(Field field, int enumLength) {
    if (String.class.equals(field.getType())) {
      boolean hasNotNull = field.getAnnotation(Nf3NotNull.class) == null;


      boolean idOrReference = field.getAnnotation(Nf3ID.class) != null
        || field.getAnnotation(Nf3ReferenceTo.class) != null;

      boolean nullable = !idOrReference && hasNotNull;

      int len = idOrReference ? 30 : 300;

      Nf3Short aShort = field.getAnnotation(Nf3Short.class);
      if (aShort != null) {
        len = 50;
      }
      Nf3Long aLong = field.getAnnotation(Nf3Long.class);
      if (aLong != null) {
        len = 2000;
      }

      if (aShort != null && aLong != null) throw new ConflictError(aShort, aLong);

      Object prev = aShort != null ? aShort : aLong;

      Nf3Length aLength = field.getAnnotation(Nf3Length.class);
      if (aLength != null) {
        len = aLength.value();
      }

      if (prev != null && aLength != null) throw new ConflictError(prev, aLength);


      prev = prev != null ? prev : aLength;

      Nf3Text aText = field.getAnnotation(Nf3Text.class);
      if (prev != null && aText != null) throw new ConflictError(prev, aText);

      if (aText != null) {
        return new DbTypeImpl(SqlType.CLOB, 0, nullable);
      }

      return new DbTypeImpl(SqlType.VARCHAR, len, nullable);
    }

    if (Integer.class.equals(field.getType())) return new DbTypeImpl(SqlType.INT, intLen(field), true);
    if (int.class.equals(field.getType())) return new DbTypeImpl(SqlType.INT, intLen(field), false);
    if (Long.class.equals(field.getType())) return new DbTypeImpl(SqlType.BIGINT, 0, true);
    if (long.class.equals(field.getType())) return new DbTypeImpl(SqlType.BIGINT, 0, false);
    if (Boolean.class.equals(field.getType())) return new DbTypeImpl(SqlType.BOOL, 0, true);
    if (boolean.class.equals(field.getType())) return new DbTypeImpl(SqlType.BOOL, 0, false);

    if (Date.class.equals(field.getType())) {
      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;
      return new DbTypeImpl(SqlType.TIMESTAMP, 0, nullable);
    }

    if (Enum.class.isAssignableFrom(field.getType())) {
      int len = 0;
      Nf3Length aLength = field.getAnnotation(Nf3Length.class);
      if (aLength != null) {
        len = aLength.value();
      }
      if (len == 0) {
        if (enumLength < 10) throw new RuntimeException("enumLength must be >= 10");
        len = enumLength;
      }
      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;
      return new DbTypeImpl(SqlType.VARCHAR, len, nullable);
    }

    if (BigDecimal.class.equals(field.getType())) {
      int len = 19;
      Nf3Length aLength = field.getAnnotation(Nf3Length.class);
      if (aLength != null) {
        len = aLength.value();
      }
      int scale = 4;
      Nf3Scale aScale = field.getAnnotation(Nf3Scale.class);
      if (aLength != null) {
        scale = aScale.value();
      }

      boolean nullable = field.getAnnotation(Nf3NotNull.class) == null;

      DbTypeImpl ret = new DbTypeImpl(SqlType.DECIMAL, len, nullable);
      ret.scale = scale;
      return ret;
    }

    throw new RuntimeException("Cannot extract DbType from " + field);
  }

  private static int intLen(Field field) {
    return field.getAnnotation(Nf3Short.class) == null ? 4 : 8;
  }
}
