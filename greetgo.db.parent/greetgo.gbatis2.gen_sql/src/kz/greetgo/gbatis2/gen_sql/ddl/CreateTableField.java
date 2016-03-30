package kz.greetgo.gbatis2.gen_sql.ddl;

import kz.greetgo.gbatis2.struct.model.SimpleEssence;

public class CreateTableField implements DdlPart {
  public final String name;
  public final SimpleEssence type;
  public final boolean notNull;
  public final FieldDefault fieldDefault;

  public CreateTableField(String name, SimpleEssence type, boolean notNull, FieldDefault fieldDefault) {
    this.name = name;
    this.type = type;
    this.notNull = notNull;
    this.fieldDefault = fieldDefault;
  }

  @Override
  public String toString() {
    return name + " " + type + (notNull ? " not null" : "") + (fieldDefault == null ? "" : " default " + fieldDefault);
  }

  @Override
  public <T> T visitPart(DdlVisitor<T> visitor) {
    return visitor.visitCreateTableField(this);
  }
}
