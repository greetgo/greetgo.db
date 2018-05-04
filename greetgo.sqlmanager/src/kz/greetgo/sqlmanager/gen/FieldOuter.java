package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.model.JavaType;

/**
 * Класс-посредник для генерации исходников поля
 * <p>
 * Примитивная реализация, потому что пока постобработки не требуется
 * </p>
 * 
 * @author pompei
 * 
 */
public class FieldOuter {
  /**
   * Тип поля
   */
  public final JavaType type;
  /**
   * Имя поля
   */
  public final String name;
  
  /**
   * Конструктор объекта
   * 
   * @param javaType
   *          инициатор типа поля
   * @param name
   *          инициатор имени поля
   */
  public FieldOuter(JavaType javaType, String name) {
    this.type = javaType;
    this.name = name;
  }
}
