package kz.greetgo.gbatis.model;

import java.util.Date;

/**
 * Получение данных отложенное на будущее
 * 
 * @author pompei
 * 
 * @param <T>
 *          тип получаемых данных
 */
public interface FutureCall<T> {
  /**
   * Полчить последние изменения
   * 
   * @return запрашиваемое значение
   */
  T last();
  
  /**
   * Получить часть последних изменений
   * <p>
   * <i>Используется, если тип получаемых данных - список</i>
   * </p>
   * 
   * @param offset
   *          количество пропускаемых данных сначала
   * @param pageSize
   *          количество получаемых данных
   * @return запрашиваемое значение
   */
  T last(int offset, int pageSize);
  
  /**
   * Получить значение на указанный момент
   * 
   * @param at
   *          момент, на который запрашивется значение
   * @return запрашиваемое значение
   */
  T at(Date at);
  
  /**
   * Получить часть значений на указанный момент
   * <p>
   * <i>Используется, если тип получаемых данных - список</i>
   * </p>
   * 
   * @param at
   *          момент, на который запрашиваются значения
   * @param offset
   *          количество пропускаемых данных сначала
   * @param pageSize
   *          количество получаемых данных
   * @return запрашиваемое значение
   */
  T at(Date at, int offset, int pageSize);
}
