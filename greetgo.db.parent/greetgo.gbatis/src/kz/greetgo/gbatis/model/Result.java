package kz.greetgo.gbatis.model;

import kz.greetgo.gbatis.futurecall.SqlViewer;

public class Result {
  
  /**
   * Тип возвращаемого результата
   */
  public ResultType type;
  
  /**
   * Java-класс, в который нужно засунуть результат
   */
  public Class<?> resultDataClass;
  
  /**
   * Имя поля для использования ключа мапы.
   * <p>
   * Используется если <nobr><code>{@link #type} == MAP</code></nobr>
   * </p>
   */
  public String mapKeyField;
  
  /**
   * Класс ключа мапы
   * <p>
   * Используется если <nobr><code>{@link #type} == MAP</code></nobr>
   * </p>
   */
  public Class<?> mapKeyClass;
  
  /**
   * Место вывода sql-ей для трэйсинга (может быть null)
   */
  public SqlViewer sqlViewer = null;
  
  /**
   * Объект для создания элемента данных. Если <code>null</code>, то используется метод newInstance
   * у класса объекта
   */
  public Creator<?> creator = null;
  
  public Result with(SqlViewer sqlViewer) {
    this.sqlViewer = sqlViewer;
    return this;
  }
  
  public Result with(Creator<?> creator) {
    this.creator = creator;
    return this;
  }
  
  public static Result setOf(Class<?> classs) {
    Result ret = new Result();
    ret.resultDataClass = classs;
    ret.type = ResultType.SET;
    return ret;
  }
  
  public static Result listOf(Class<?> classs) {
    Result ret = new Result();
    ret.resultDataClass = classs;
    ret.type = ResultType.LIST;
    return ret;
  }
  
  public static Result simple(Class<?> classs) {
    Result ret = new Result();
    ret.resultDataClass = classs;
    ret.type = ResultType.SIMPLE;
    return ret;
  }
  
  public static Result mapOf(String keyFieldName, Class<?> keyClass, Class<?> valueClass) {
    Result ret = new Result();
    ret.resultDataClass = valueClass;
    ret.type = ResultType.MAP;
    ret.mapKeyClass = keyClass;
    ret.mapKeyField = keyFieldName;
    return ret;
  }
}
