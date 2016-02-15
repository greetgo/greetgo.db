package kz.greetgo.gbatis.futurecall;

/**
 * Ошибка невозможности поиска параметра
 * 
 * @author pompei
 * 
 */
public class CannotFindParamException extends RuntimeException {
  
  public CannotFindParamException() {}
  
  public CannotFindParamException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public CannotFindParamException(String message) {
    super(message);
  }
  
  public CannotFindParamException(Throwable cause) {
    super(cause);
  }
  
}
