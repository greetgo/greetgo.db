package kz.greetgo.gbatis2.struct;

import kz.greetgo.util.ServerUtil;

public class OptionDot {
  public String key;
  public String value;
  public Place place;

  public OptionDot(String key, String value, Place place) {
    this.key = key;
    this.value = ServerUtil.trim(value);
    this.place = place;
  }
}
