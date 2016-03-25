package kz.greetgo.gbatis2.struct;

import kz.greetgo.util.ServerUtil;

import java.util.ArrayList;
import java.util.List;

public class ParsedEssence {
  public final String subpackage;
  public final String name;
  public final String type;
  public final String comment;
  public final Place place;

  public final List<ParsedField> fieldList = new ArrayList<>();

  public ParsedEssence(String subpackage, String name, String type, String comment, Place place) {
    this.subpackage = ServerUtil.trim(subpackage);
    this.name = ServerUtil.trim(name);
    this.type = ServerUtil.trim(type);
    this.comment = ServerUtil.trim(comment);
    this.place = place;
  }
}
