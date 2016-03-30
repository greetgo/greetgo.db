package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.AliasDot;
import kz.greetgo.gbatis2.struct.EnumDot;

public class AliasOverlapEnum extends GbatisException {
  public final AliasDot aliasDot;
  public final EnumDot enumDot;

  public AliasOverlapEnum(AliasDot aliasDot, EnumDot enumDot) {
    super(aliasDot.name + ", alias at " + aliasDot.place.placement() + ", enum at " + enumDot.place.placement());
    this.aliasDot = aliasDot;
    this.enumDot = enumDot;
  }
}
