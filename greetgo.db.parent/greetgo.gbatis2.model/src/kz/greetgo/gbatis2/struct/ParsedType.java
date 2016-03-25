package kz.greetgo.gbatis2.struct;

import java.util.ArrayList;
import java.util.List;

public class ParsedType {
  public final String subpackage;
  public final String name;
  public final String type;
  public final String comment;
  public final Place place;

  public final List<ParsedField> fieldList = new ArrayList<>();

  public ParsedType(String subpackage, String name, String type, String comment, Place place) {
    this.subpackage = UtilGbatis.trim(subpackage);
    this.name = UtilGbatis.trim(name);
    this.type = UtilGbatis.trim(type);
    this.comment = UtilGbatis.trim(comment);
    this.place = place;
  }

  public String placement() {
    return place.placement();
  }
}
