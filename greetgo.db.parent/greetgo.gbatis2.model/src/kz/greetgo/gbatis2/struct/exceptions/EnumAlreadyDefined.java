package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.Place;

public class EnumAlreadyDefined extends GbatisException {
  public final String name;
  public final Place place1;
  public final Place place2;

  public EnumAlreadyDefined(String name, Place place1, Place place2) {
    super("name = " + name + ", place1 = " + place1 + ", place2 = " + place2);
    this.name = name;
    this.place1 = place1;
    this.place2 = place2;
  }
}
