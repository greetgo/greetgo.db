package kz.greetgo.gbatis.util.model;

public class TableReference {
  public String fromTableName, fromColName;
  public boolean fromCanNull;
  
  public String toTableName;
  
  public ForeignKey from() {
    return new ForeignKey(fromTableName, fromColName, fromCanNull);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (fromCanNull ? 1231 :1237);
    result = prime * result + ((fromColName == null) ? 0 :fromColName.hashCode());
    result = prime * result + ((fromTableName == null) ? 0 :fromTableName.hashCode());
    result = prime * result + ((toTableName == null) ? 0 :toTableName.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TableReference other = (TableReference)obj;
    if (fromCanNull != other.fromCanNull) return false;
    if (fromColName == null) {
      if (other.fromColName != null) return false;
    } else if (!fromColName.equals(other.fromColName)) return false;
    if (fromTableName == null) {
      if (other.fromTableName != null) return false;
    } else if (!fromTableName.equals(other.fromTableName)) return false;
    if (toTableName == null) {
      if (other.toTableName != null) return false;
    } else if (!toTableName.equals(other.toTableName)) return false;
    return true;
  }
}
