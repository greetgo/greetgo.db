package kz.greetgo.gbatis.model;

/**
 * Хранит информацию параметра в SQL-запросе
 * 
 * @author pompei
 */
public class Param {
  public Class<?> type;
  public String name;
  
  public Param() {}
  
  public Param(Class<?> type, String name) {
    this.type = type;
    this.name = name;
  }
  
  @Override
  public String toString() {
    return "param " + name + " " + type.getSimpleName();
  }
}
