package kz.greetgo.gbatis2.struct;

import kz.greetgo.util.ServerUtil;

public class ParsedField {
  public final String name, type, comment;
  public final boolean key;
  public final Place place;

  public ParsedField(String keyStr, String name, String type, String comment, Place place) {
    key = keyStr != null;
    this.name = ServerUtil.trim(name);
    this.type = ServerUtil.trim(type);
    this.comment = ServerUtil.trim(comment);
    this.place = place;
  }
}
