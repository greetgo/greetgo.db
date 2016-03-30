package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.EnumDot;

public class EnumOverlapStdType extends GbatisException {
  public final EnumDot enumDot;

  public EnumOverlapStdType(EnumDot enumDot) {
    super(enumDot.name + " at " + enumDot.place.placement());
    this.enumDot = enumDot;
  }
}
