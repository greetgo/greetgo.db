package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.EnumClassNotFound;

public class EnumDot {
  public final String name;
  public final Class<? extends Enum> enumClass;
  public final Place place;

  public EnumDot(String name, String enumClassName, Place place) {
    this.name = name;
    this.enumClass = loadClass(enumClassName, place);
    this.place = place;
  }

  private static Class<? extends Enum> loadClass(String enumClassName, Place place) {
    try {
      //noinspection unchecked
      return (Class<? extends Enum>) Class.forName(enumClassName);
    } catch (ClassNotFoundException e) {
      throw new EnumClassNotFound(enumClassName, place, e);
    }
  }
}
