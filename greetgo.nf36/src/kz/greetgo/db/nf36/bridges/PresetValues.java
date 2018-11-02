package kz.greetgo.db.nf36.bridges;

import java.util.HashMap;
import java.util.Map;

public class PresetValues {

  private final Map<String, Object> valueMap = new HashMap<>();

  public void presetValue(String fieldName, Object value) {
    valueMap.put(fieldName, value);
  }

  public boolean exists(String fieldName) {
    return valueMap.containsKey(fieldName);
  }

  public Object getValue(String fieldName) {
    if (!exists(fieldName)) {
      throw new RuntimeException("No preset value for " + fieldName);
    }
    return valueMap.get(fieldName);
  }
}
