package kz.greetgo.gbatis2.struct.model;

public class KeyField {

  public final DbField source;

  public KeyField(DbField source) {
    if (!(source.type instanceof SimpleEssence)) throw new IllegalArgumentException();
    if (!source.key) throw new IllegalArgumentException();
    this.source = source;
  }

  @Override
  public String toString() {
    return "key{" + name() + ' ' + type() + '}';
  }

  public SimpleEssence type() {
    return (SimpleEssence) source.type;
  }

  public String name() {
    return source.name;
  }
}
