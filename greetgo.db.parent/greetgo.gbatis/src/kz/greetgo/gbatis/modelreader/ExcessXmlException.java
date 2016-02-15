package kz.greetgo.gbatis.modelreader;

/**
 * Ошибка избыточного SQL-я (непонятно какой использовать)
 * 
 * @author pompei
 */
public class ExcessXmlException extends RuntimeException {
  
  public ExcessXmlException() {}
  
  public ExcessXmlException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ExcessXmlException(String message) {
    super(message);
  }
  
  public ExcessXmlException(Throwable cause) {
    super(cause);
  }
  
}
