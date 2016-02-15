package kz.greetgo.sqlmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранит данные поля в NF3-нотации
 * 
 * @author pompei
 * 
 */
public class Field {
  public final Table table;
  public final String name;
  public final Type type;
  
  public Field(Table table, String name, Type type) {
    this.table = table;
    this.name = name;
    this.type = type;
  }
  
  @Override
  public String toString() {
    return name + (type == null ? "" :" " + type.name);
  }
  
  /**
   * Формирует и возвращает поля БД для данного поля в NF3-нотации
   * 
   * @return список полей БД
   */
  public List<FieldDb> dbFields() {
    List<FieldDb> ret = new ArrayList<>();
    
    List<SimpleType> stypes = new ArrayList<>();
    List<JavaType> jtypes = new ArrayList<>();
    type.assignSimpleTypes(stypes);
    type.assignJavaTypes(jtypes);
    for (int i = 0, C = stypes.size(); i < C; i++) {
      ret.add(new FieldDb(stypes.get(i), jtypes.get(i), name + (C == 1 ? "" :"" + (i + 1))));
    }
    
    return ret;
  }
}
