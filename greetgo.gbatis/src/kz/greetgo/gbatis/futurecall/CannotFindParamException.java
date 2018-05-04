package kz.greetgo.gbatis.futurecall;

/**
 * Ошибка невозможности поиска параметра
 *
 * @author pompei
 */
public class CannotFindParamException extends RuntimeException {
  public CannotFindParamException(String message) {
    super(message);
  }
}
