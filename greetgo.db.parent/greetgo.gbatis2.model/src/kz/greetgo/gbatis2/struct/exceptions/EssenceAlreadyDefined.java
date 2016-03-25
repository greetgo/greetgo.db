package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.ParsedEssence;

public class EssenceAlreadyDefined extends GbatisException {
  public final ParsedEssence currentType;
  public final ParsedEssence alreadyExistsType;

  public EssenceAlreadyDefined(ParsedEssence currentType, ParsedEssence alreadyExistsType) {
    super(currentType.name + ", currentTypePlace = " + currentType.place.placement()
        + ", alreadyExistsTypePlace = " + alreadyExistsType.place.placement());
    this.currentType = currentType;
    this.alreadyExistsType = alreadyExistsType;
  }
}
