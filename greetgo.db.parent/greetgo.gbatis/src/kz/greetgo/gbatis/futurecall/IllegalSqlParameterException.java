package kz.greetgo.gbatis.futurecall;

/**
 * Ошибка некоррекнтости параметра в SQL-запросе GBatis-а
 * 
 * @author pompei
 */
public class IllegalSqlParameterException extends RuntimeException {
  
  public IllegalSqlParameterException() {}
  
  public IllegalSqlParameterException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public IllegalSqlParameterException(String message) {
    super(message);
  }
  
  public IllegalSqlParameterException(Throwable cause) {
    super(cause);
  }
  
}
