package kz.greetgo.sqlmanager.parser;

/**
 * Ошибка парсинга NF3-файлов
 * 
 * @author pompei
 */
public class StruParseException extends RuntimeException {
  
  public StruParseException() {}
  
  public StruParseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
  public StruParseException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public StruParseException(String message) {
    super(message);
  }
  
  public StruParseException(Throwable cause) {
    super(cause);
  }
  
}
