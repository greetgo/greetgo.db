package kz.greetgo.gbatis.util;

/**
 * Установщик значения
 * 
 * @author pompei
 */
public interface Setter {
  /**
   * Устанавливает значение
   * 
   * @param value
   *          устанавливаемое значение
   */
  void set(Object value);
  
  /**
   * Получает имя установщика
   * <p>
   * Это может быть имя поля, для которого создан установщик
   * </p>
   * 
   * @return имя установщика
   */
  String name();
  
  /**
   * Получает тип данных в установщике
   * 
   * @return тип данных в установщике
   */
  Class<?> type();
}
