package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.AliasDot;
import kz.greetgo.gbatis2.struct.ParsedEssence;

public class AliasOverlapEssence extends GbatisException {
  public final AliasDot aliasDot;
  public final ParsedEssence parsedEssence;

  public AliasOverlapEssence(AliasDot aliasDot, ParsedEssence parsedEssence) {
    super(aliasDot.name + ", alias at " + aliasDot.place.placement() + ", essence at " + parsedEssence.place.placement());
    this.aliasDot = aliasDot;
    this.parsedEssence = parsedEssence;
  }
}
