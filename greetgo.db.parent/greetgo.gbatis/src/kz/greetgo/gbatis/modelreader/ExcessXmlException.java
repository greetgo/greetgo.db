package kz.greetgo.gbatis.modelreader;

/**
 * Ошибка избыточного SQL-я (непонятно какой использовать)
 *
 * @author pompei
 */
public class ExcessXmlException extends RuntimeException {
  public ExcessXmlException(String message) {
    super(message);
  }
}
