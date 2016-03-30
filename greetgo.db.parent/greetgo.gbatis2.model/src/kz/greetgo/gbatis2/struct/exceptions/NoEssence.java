package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.Place;

import java.util.Objects;

public class NoEssence extends GbatisException {

  public final String writtenType;
  public final String unAliasedType;
  public final Place place;

  public NoEssence(String writtenType, String unAliasedType, Place place) {
    super(createMessage(writtenType, unAliasedType, place));
    this.writtenType = writtenType;
    this.unAliasedType = unAliasedType;
    this.place = place;
  }

  private static String createMessage(String writtenType, String unAliasedType, Place place) {
    if (Objects.equals(writtenType, unAliasedType)) {
      return writtenType + " at " + place.placement();
    } else {
      return unAliasedType + " (alias " + unAliasedType + ") at " + place.placement();
    }
  }

}
