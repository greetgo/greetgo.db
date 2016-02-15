package kz.greetgo.sqlmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Таблица в NF3-нотации
 * 
 * @author pompei
 */
public class Table extends Type {
  public final List<Field> keys = new ArrayList<>();
  public final List<Field> fields = new ArrayList<>();
  
  public long sequenceFrom = 0;
  
  public Table(String name, String subpackage) {
    super(name, subpackage);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("table ").append(name);
    if (sequenceFrom > 0) {
      sb.append(" from ").append(sequenceFrom);
    }
    sb.append(keys);
    for (Field f : fields) {
      sb.append(" /").append(f);
    }
    return sb.toString();
  }
  
  @Override
  public void assignSimpleTypes(List<SimpleType> types) {
    for (Field field : keys) {
      field.type.assignSimpleTypes(types);
    }
  }
  
  @Override
  public void assignJavaTypes(List<JavaType> types) {
    for (Field field : keys) {
      field.type.assignJavaTypes(types);
    }
  }
  
  /**
   * Формирует и возвращает список наименований ключевых полей в БД
   * 
   * @return список наименований ключевых полей в БД
   */
  public List<String> keyNames() {
    List<String> ret = new ArrayList<>();
    for (Field field : keys) {
      List<SimpleType> types = new ArrayList<>();
      field.type.assignSimpleTypes(types);
      if (types.size() == 1) {
        ret.add(field.name);
      } else {
        for (int i = 1, C = types.size(); i <= C; i++) {
          ret.add(field.name + i);
        }
      }
    }
    return ret;
  }
  
  public boolean hasSequence() {
    if (keys.size() != 1) return false;
    Type keyType = keys.get(0).type;
    return keyType instanceof SimpleType && ((SimpleType)keyType).needSequence;
  }
  
  /**
   * Формирует и возвращает список ключевых полей в БД
   * 
   * @return список ключевых полей в БД
   */
  public List<FieldDb> dbKeys() {
    List<FieldDb> ret = new ArrayList<>();
    
    for (Field f : keys) {
      List<SimpleType> types = new ArrayList<>();
      List<JavaType> javaTypes = new ArrayList<>();
      f.type.assignSimpleTypes(types);
      f.type.assignJavaTypes(javaTypes);
      for (int i = 1, C = types.size(); i <= C; i++) {
        String name = f.name + (C == 1 ? "" :"" + i);
        SimpleType stype = types.get(i - 1);
        JavaType javaType = javaTypes.get(i - 1);
        ret.add(new FieldDb(stype, javaType, name));
      }
    }
    
    return ret;
  }
  
  public String subpackage() {
    if (subpackage == null) return "";
    return "." + subpackage;
  }
}
