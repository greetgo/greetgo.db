package kz.greetgo.sqlmanager.model;

import java.util.ArrayList;
import java.util.List;

import kz.greetgo.sqlmanager.model.command.Command;

/**
 * Определяет тип в NF3-нотации для связи с Java, и SQL, и между ними
 * 
 * @author pompei
 */
public abstract class Type {
  public final String name;
  public final String subpackage;
  
  public final List<Command> commands = new ArrayList<>();
  
  public Type(String name, String subpackage) {
    this.name = name;
    this.subpackage = subpackage;
  }
  
  public abstract void assignSimpleTypes(List<SimpleType> types);
  
  public abstract void assignJavaTypes(List<JavaType> types);
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 :name.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Type other = (Type)obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }
}
