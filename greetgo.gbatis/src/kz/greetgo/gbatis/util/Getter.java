package kz.greetgo.gbatis.util;

/**
 * Интерфейс произвольного геттера
 * 
 * @author pompei
 * 
 * @param <T>
 *          получаемый тип
 */
public interface Getter<T> {
  /**
   * Метод получения
   * 
   * @return полученое значение
   */
  T get();
}
