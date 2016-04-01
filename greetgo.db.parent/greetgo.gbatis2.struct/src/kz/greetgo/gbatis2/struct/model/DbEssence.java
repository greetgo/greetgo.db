package kz.greetgo.gbatis2.struct.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbEssence implements Essence {
  public final String subpackage, name, comment;
  public final List<DbField> fieldList = new ArrayList<>();
  public boolean keyFromEssence = false;

  public DbEssence(String subpackage, String name, String comment) {
    this.subpackage = subpackage;
    this.name = name;
    this.comment = comment;
  }

  @Override
  public boolean isSequential() {
    for (DbField dbField : fieldList) {
      if (dbField.key && dbField.type.isSequential()) return true;
    }
    return false;
  }

  List<KeyField> keyFieldsCache = null;

  public List<KeyField> keyFields() {
    if (keyFieldsCache != null) return keyFieldsCache;

    List<KeyField> ret = new ArrayList<>();

    for (DbField dbField : fieldList) {
      if (dbField.key) {

        if (dbField.type instanceof SimpleEssence) {
          ret.add(new KeyField(dbField));
          continue;
        }

        if (dbField.type instanceof DbEssence) {
          DbEssence typeEssence = (DbEssence) dbField.type;
          ret.addAll(typeEssence.keyFields());
          continue;
        }

        throw new RuntimeException("Unknown essence class: " + dbField.type);
      }
    }

    return keyFieldsCache = ret;
  }

  private List<SimpleEssence> simpleEssenceListCache = null;

  @Override
  public List<SimpleEssence> simpleEssenceList() {
    if (simpleEssenceListCache == null) {
      List<SimpleEssence> ret = new ArrayList<>();
      for (KeyField keyField : keyFields()) {
        ret.add(keyField.type());
      }
      simpleEssenceListCache = Collections.unmodifiableList(ret);
    }

    return simpleEssenceListCache;
  }
}
