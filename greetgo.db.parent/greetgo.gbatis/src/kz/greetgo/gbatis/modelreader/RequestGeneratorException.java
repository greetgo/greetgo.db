package kz.greetgo.gbatis.modelreader;

/**
 * Ошибка при генерации запроса
 * 
 * @author pompei
 */
public class RequestGeneratorException extends RuntimeException {
  
  public RequestGeneratorException() {}
  
  public RequestGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public RequestGeneratorException(String message) {
    super(message);
  }
  
  public RequestGeneratorException(Throwable cause) {
    super(cause);
  }
  
}
