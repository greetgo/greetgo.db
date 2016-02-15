package kz.greetgo.gbatis.util.model;

public class ForeignKey {
  public String tableName, colName;
  public boolean canNull;
  
  public ForeignKey() {}
  
  public ForeignKey(String tableName, String colName, boolean canNull) {
    this.tableName = tableName;
    this.colName = colName;
    this.canNull = canNull;
  }
  
  @Override
  public String toString() {
    return "ForeignKey [tableName=" + tableName + ", colName=" + colName + ", canNull=" + canNull
        + "]";
  }
}
