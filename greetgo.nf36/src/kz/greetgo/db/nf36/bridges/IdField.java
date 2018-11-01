package kz.greetgo.db.nf36.bridges;

import java.util.function.Function;

public class IdField {
  private final String name;
  private String realName;

  public IdField(String name) {
    this.name = realName = name;
  }

  public String name() {
    return realName;
  }

  public void applyConverter(Function<String, String> nameConverter) {
    realName = nameConverter.apply(name);
  }
}
