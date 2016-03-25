package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.Option;

public class OptionAlreadyDefined extends GbatisException {
  public final Option current;
  public final Option alreadyDefined;

  public OptionAlreadyDefined(Option current, Option alreadyDefined) {
    super(current.key + " at " + current.place.placement() + " and at " + alreadyDefined.place.placement());
    this.current = current;
    this.alreadyDefined = alreadyDefined;
  }
}
