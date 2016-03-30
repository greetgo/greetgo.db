package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.Place;

public class MustNotBeUsedTwoWaysOfDefiningKeyFields extends GbatisException {
  public final String essenceName;
  public final String keyFieldName;
  public final Place place;

  public MustNotBeUsedTwoWaysOfDefiningKeyFields(String essenceName, String keyFieldName, Place place) {
    super(essenceName + "." + keyFieldName + " at " + place.placement());
    this.essenceName = essenceName;
    this.keyFieldName = keyFieldName;
    this.place = place;
  }
}
