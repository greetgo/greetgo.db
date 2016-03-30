package kz.greetgo.gbatis2.struct.model;

public class EnumEssence implements Essence {
  public final Class<? extends Enum> source;

  public EnumEssence(Class<? extends Enum> source) {
    this.source = source;
  }
}
