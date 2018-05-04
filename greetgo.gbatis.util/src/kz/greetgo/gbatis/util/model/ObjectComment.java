package kz.greetgo.gbatis.util.model;

public class ObjectComment {
  public String name, comment;
  
  public ObjectComment() {}
  
  public ObjectComment(String name, String comment) {
    this.name = name;
    this.comment = comment;
  }
  
  @Override
  public String toString() {
    return "ObjectComment [surname=" + name + ", comment=" + comment + "]";
  }
}
