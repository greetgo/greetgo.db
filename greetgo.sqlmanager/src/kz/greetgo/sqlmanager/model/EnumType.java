package kz.greetgo.sqlmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрагирует тип енума
 * 
 * @author pompei
 * 
 */
public class EnumType extends Type implements JavaType {
  public final String as;
  public final List<String> values = new ArrayList<>();
  
  public String pack;
  
  public String pack() {
    if (subpackage == null) return pack == null ? "" :pack;
    return pack + "." + subpackage;
  }
  
  public EnumType(String name, String subpackage, String as, String values[]) {
    super(name, subpackage);
    if (as == null && (values == null || values.length == 0)) {
      throw new IllegalArgumentException("One of 'values' or 'as' must be defined");
    }
    if (as != null && values != null && values.length > 0) {
      throw new IllegalArgumentException("Both 'as' and 'values' have been defined. Must be one of");
    }
    this.as = as;
    for (String value : values) {
      this.values.add(value);
    }
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("enum ").append(name);
    if (as != null) {
      sb.append(" as ").append(as);
    }
    if (values != null) for (String v : values) {
      sb.append(' ').append(v);
    }
    return sb.toString();
  }
  
  @Override
  public void assignSimpleTypes(List<SimpleType> types) {
    types.add(SimpleType.tword);
  }
  
  @Override
  public void assignJavaTypes(List<JavaType> types) {
    types.add(this);
  }
  
  private transient String objectType = null;
  
  @Override
  public String objectType() {
    if (objectType != null) return objectType;
    
    if (as != null) return objectType = as;
    
    String sp = "";
    if (subpackage != null) {
      sp = subpackage + ".";
    }
    
    return objectType = (pack == null ? sp :pack + "." + sp) + name.substring(0, 1).toUpperCase()
        + name.substring(1);
  }
  
  @Override
  public String javaType() {
    return objectType();
  }
}
