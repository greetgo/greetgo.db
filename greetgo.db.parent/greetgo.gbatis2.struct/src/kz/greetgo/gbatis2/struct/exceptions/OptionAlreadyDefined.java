package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.OptionDot;

public class OptionAlreadyDefined extends GbatisException {
  public final OptionDot current;
  public final OptionDot alreadyDefined;

  public OptionAlreadyDefined(OptionDot current, OptionDot alreadyDefined) {
    super(current.key + " at " + current.place.placement() + " and at " + alreadyDefined.place.placement());
    this.current = current;
    this.alreadyDefined = alreadyDefined;
  }
}
