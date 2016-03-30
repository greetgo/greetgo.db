package kz.greetgo.gbatis2.struct.model;

public class DbField {
  public final String name;
  public final Essence type;
  public final boolean key;
  public final String comment;

  public DbField(boolean key, String name, Essence type, String comment) {
    this.name = name;
    this.type = type;
    this.key = key;
    this.comment = comment;
  }
}
