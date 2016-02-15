package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.model.SimpleType;

/**
 * Диалект БД
 * 
 * <p>
 * Определает значения параметров, которые отличаются для различных БД
 * </p>
 * 
 * @author pompei
 */
public interface SqlDialect {
  
  /**
   * Определяет SQL-тип для указанного NF3-типа, который используется в DDL-выражениях
   * 
   * @param simpleType
   *          исходный NF3-тип
   * @return SQL-тип для DDL-выражений
   */
  String sqlType(SimpleType simpleType);
  
  /**
   * Определяет SQL-тип для указанного NF3-типа, который используется в хранимых процедур/функций
   * 
   * @param simpleType
   *          исходный NF3-тип
   * @return SQL-тип, который используется в хранимых процедур/функций
   */
  String procType(SimpleType simpleType);
  
  /**
   * Получает тип timestamp (может отличатся для различных БД)
   * 
   * @return тип timestamp
   */
  String timestamp();
  
  /**
   * Получает имя функции по определению текущего момента времени
   * 
   * @return имя функции по определению текущего момента времени
   */
  String current_timestamp();
  
  /**
   * Получает строку типа для хранения ИД того, кто делает изменения в БД
   * 
   * @param userIdFieldType
   *          тип для хранения ИД того, кто делает изменения в БД
   * @param userIdLength
   *          длина типа
   * @return строка типа для хранения ИД того, кто делает изменения в БД
   */
  String userIdFieldType(UserIdFieldType userIdFieldType, int userIdLength);
  
}
