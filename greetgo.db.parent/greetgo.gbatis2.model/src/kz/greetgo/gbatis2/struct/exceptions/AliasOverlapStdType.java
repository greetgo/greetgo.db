package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.AliasDot;

public class AliasOverlapStdType extends GbatisException {
  public final AliasDot aliasDot;

  public AliasOverlapStdType(AliasDot aliasDot) {
    super(aliasDot.name + " at " + aliasDot.place.placement());
    this.aliasDot = aliasDot;
  }
}
