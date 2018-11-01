package kz.greetgo.db.nf36.bridges;

import java.util.HashMap;
import java.util.Map;

public class AliasMapper {
  private final Map<String, String> map = new HashMap<>();

  public void addAlias(String fieldName, String alias) {
    map.put(fieldName, alias);
  }

  public String convert(String fieldName) {
    String newName = map.get(fieldName);
    return newName == null ? fieldName : newName;
  }
}
