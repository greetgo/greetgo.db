package kz.greetgo.gbatis.util.iface;

import java.util.Date;
import java.util.List;

import kz.greetgo.gbatis.model.Creator;

public interface UtilRegister {
  void cleanTables(String... tableName);
  
  <T> T getField(Class<T> cl, String tableName, String gettingField, Object... fieldValuePairs);
  
  String getStrField(String tableName, String gettingField, Object... fieldValuePairs);
  
  Long getLongField(String tableName, String gettingField, Object... fieldValuePairs);
  
  Integer getIntField(String tableName, String gettingField, Object... fieldValuePairs);
  
  Date getTimeField(String tableName, String gettingField, Object... fieldValuePairs);
  
  Double getDoubleField(String tableName, String gettingField, Object... fieldValuePairs);
  
  boolean getBoolField(String tableName, String gettingField, Object... fieldValuePairs);
  
  <T> T insert(String tableName, T object);
  
  int update(String tableName, Object object);
  
  int deleteWhere(String tableName, String where, Object... values);
  
  int countWhere(String tableName, String where, Object... values);
  
  /**
   * Проверяет наличие записи по ключевым полям, значения которых беруться из объекта
   * 
   * @param tableName
   *          таблица, в которой проверяется наличие записи
   * @param object
   *          объект со значением ключевых полей
   * @return признак наличия записи
   */
  boolean existsKey(String tableName, Object object);
  
  <T> List<T> selectList(Class<T> cl, CharSequence sql, List<Object> params);
  
  <T> List<T> selectList(Creator<T> creator, CharSequence sql, List<Object> params);
  
  <T> List<T> seleList(Class<T> cl, CharSequence sql, Object... params);
  
  <T> List<T> seleList(Creator<T> cl, CharSequence sql, Object... params);
  
  <T> List<T> selectList(Class<T> cl, CharSequence sql, List<Object> params, int offset, int count);
  
  <T> List<T> selectList(Creator<T> creator, CharSequence sql, List<Object> params, //
      int offset, int count);
  
  long selectLong(CharSequence sql, List<Object> params);
  
  long seleLong(CharSequence sql, Object... params);
  
  int selectInt(CharSequence sql, List<Object> params);
  
  int seleInt(CharSequence sql, Object... params);
  
  /**
   * По ключевым полям проверяет наличие объекта в БД: если находит, то обновляет остальные поля,
   * иначе вставляет новую запись
   * 
   * @param tableName
   *          имя таблицы
   * @param object
   *          сохраняемый объект
   * @return возвращает true если произошол insert, иначе - false;
   */
  boolean save(String tableName, Object object);
  
  /**
   * Вызывает insert если insert == true, иначе вызывает update
   * 
   * @param tableName
   *          имя таблицы
   * @param insert
   *          признак вставки
   * @param object
   *          объект сохранения
   * @return объект сохранения
   */
  <T> T save(String tableName, boolean insert, T object);
  
  int execUpdate(CharSequence sql, Object... params);
  
  int executeUpdate(CharSequence sql, List<Object> params);
}
