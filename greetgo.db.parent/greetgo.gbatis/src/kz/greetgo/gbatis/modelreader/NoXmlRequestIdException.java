package kz.greetgo.gbatis.modelreader;

/**
 * Не найден идентификатор запроса
 *
 * @author pompei
 */
public class NoXmlRequestIdException extends RuntimeException {
  public NoXmlRequestIdException(String message) {
    super(message);
  }
}
