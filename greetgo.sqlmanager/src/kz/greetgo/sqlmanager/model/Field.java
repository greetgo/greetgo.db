package kz.greetgo.sqlmanager.model;

import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.util.ServerUtil.firstUpper;

/**
 * Хранит данные поля в NF3-нотации
 *
 * @author pompei
 */
public class Field {
  public final Table table;
  public final String name;
  public final Type type;
  
  public String changeClassName = null;

  public Field(Table table, String name, Type type) {
    this.table = table;
    this.name = name;
    this.type = type;
  }

  @Override
  public String toString() {
    return name + (type == null ? "" : " " + type.name);
  }

  /**
   * Формирует и возвращает поля БД для данного поля в NF3-нотации
   *
   * @return список полей БД
   */
  public List<FieldDb> dbFields() {
    List<FieldDb> ret = new ArrayList<>();

    List<SimpleType> sTypes = new ArrayList<>();
    List<JavaType> jTypes = new ArrayList<>();
    type.assignSimpleTypes(sTypes);
    type.assignJavaTypes(jTypes);
    for (int i = 0, C = sTypes.size(); i < C; i++) {
      ret.add(new FieldDb(sTypes.get(i), jTypes.get(i), name + (C == 1 ? "" : "" + (i + 1))));
    }

    return ret;
  }

  public String javaTableFieldName() {
    return firstUpper(table.name) + firstUpper(name);
  }
}
