package kz.greetgo.gbatis.model;

/**
 * Тип запроса в БД
 * 
 * @author pompei
 */
public enum RequestType {
  /**
   * Запрос на получение данных: селект или селект с with-конструкцией
   */
  Sele,
  
  /**
   * Вызов хранимой процедуры
   */
  Call,
  
  /**
   * Запрос на изменение данных
   */
  Modi;
}
