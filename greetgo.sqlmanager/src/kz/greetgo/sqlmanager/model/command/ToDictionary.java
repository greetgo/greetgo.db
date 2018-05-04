package kz.greetgo.sqlmanager.model.command;

/**
 * Дополнительная команда для генерации метода для получения объекта специального класса
 * 
 * @author pompei
 */
public class ToDictionary extends Command {
  public final String toClass;
  public final String more;
  
  public ToDictionary(String toClass, String more) {
    this.toClass = toClass;
    this.more = more;
  }
  
  @Override
  public String toString() {
    return "ToDictionary [toClass=" + toClass + ", more=" + more + "]";
  }
}
