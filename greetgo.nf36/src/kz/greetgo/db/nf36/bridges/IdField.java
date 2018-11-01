package kz.greetgo.db.nf36.bridges;

import java.util.function.Function;

public class IdField {
  public final String name;
  private String alias;

  public IdField(String name) {
    this.name = alias = name;
  }

  public String alias() {
    return alias;
  }

  public void applyConverter(Function<String, String> nameConverter) {
    alias = nameConverter.apply(name);
  }
}
