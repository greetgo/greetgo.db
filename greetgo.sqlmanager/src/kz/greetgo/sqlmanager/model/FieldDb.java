package kz.greetgo.sqlmanager.model;

public class FieldDb {
  public final SimpleType stype;
  public final JavaType javaType;
  public final String name;
  
  public FieldDb(SimpleType stype, JavaType javaType, String name) {
    this.stype = stype;
    this.javaType = javaType;
    this.name = name;
  }
}
