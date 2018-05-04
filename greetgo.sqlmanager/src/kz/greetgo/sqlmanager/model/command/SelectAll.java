package kz.greetgo.sqlmanager.model.command;

/**
 * Дополнительная команда для генерации метода в Dao-интерфейсе, который выбирает все значения
 * NF3-таблицы
 * 
 * @author pompei
 */
public class SelectAll extends Command {
  public final String methodName;
  public final String orderBy;
  
  public SelectAll(String methodName, String orderBy) {
    this.methodName = methodName;
    this.orderBy = orderBy;
  }
  
  @Override
  public String toString() {
    return "SelectAll [methodName=" + methodName + ", orderBy=" + orderBy + "]";
  }
  
  public String orderBy() {
    if (orderBy == null) return "";
    if (orderBy.trim().length() == 0) return "";
    return "order by " + orderBy;
  }
}
