package kz.greetgo.db.nf36.errors;

/**
 * Ошибка невозможности конвертации со стороны SQL
 *
 * @author pompei
 */
public class CannotConvertFromSql extends RuntimeException {
  public CannotConvertFromSql(Object value, Class<?> toType) {
    super("Cannot convert from " + value.getClass() + " to " + toType);
  }

  public CannotConvertFromSql(String message, Class<?> fromType, Class<?> toType) {
    super(message + ": fromType = " + fromType + "; toType = " + toType);
  }
}