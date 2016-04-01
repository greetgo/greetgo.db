package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.model.EnumEssence;

public class EnumDot {
  public final String name;
  public final String enumClassName;
  public final Place place;

  public EnumDot(String name, String enumClassName, Place place) {
    this.name = name;
    this.enumClassName = enumClassName;
    this.place = place;
  }

  private EnumEssence essenceCache = null;

  public EnumEssence essence() {
    if (essenceCache != null) return essenceCache;
    return essenceCache = new EnumEssence(enumClassName, place);
  }
}
