package kz.greetgo.gbatis2.struct.model;

import java.util.ArrayList;
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
}
