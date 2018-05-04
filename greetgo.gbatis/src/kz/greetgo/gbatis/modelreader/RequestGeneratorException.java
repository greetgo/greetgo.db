package kz.greetgo.gbatis.modelreader;

/**
 * Ошибка при генерации запроса
 *
 * @author pompei
 */
public class RequestGeneratorException extends RuntimeException {
  public RequestGeneratorException(String message) {
    super(message);
  }
}
