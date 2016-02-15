package kz.greetgo.gbatis.futurecall;

/**
 * Ошибка некоррекнтости параметра в SQL-запросе GBatis-а
 *
 * @author pompei
 */
public class IllegalSqlParameterException extends RuntimeException {
  public IllegalSqlParameterException(String message) {
    super(message);
  }
}
