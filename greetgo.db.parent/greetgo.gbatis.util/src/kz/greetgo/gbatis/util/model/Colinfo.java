package kz.greetgo.gbatis.util.model;

public class Colinfo {
  public String name;
  public String type;
  public boolean canNull;
  public int nomer;
  
  public boolean isStr() {
    if (type == null) return false;
    String s = type.toLowerCase();
    if (s.contains("char")) return true;
    return false;
  }
  
  public boolean isNum() {
    if (type == null) return false;
    String s = type.toLowerCase();
    if (s.contains("int")) return true;
    if (s.contains("num")) return true;
    if (s.contains("double")) return true;
    return false;
  }
}
