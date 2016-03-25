package kz.greetgo.gbatis2.struct;

public class ParsedField {
  public final String name, type, comment;
  public final boolean key;
  public final Place place;

  public ParsedField(String keyStr, String name, String type, String comment, Place place) {
    key = keyStr != null;
    this.name = UtilGbatis.trim(name);
    this.type = UtilGbatis.trim(type);
    this.comment = UtilGbatis.trim(comment);
    this.place = place;
  }
}
