package kz.greetgo.gbatis.util.impl;

import java.util.*;

public class TableModification {
  public final String tableName;
  public final List<String> keys = new ArrayList<>();

  public TableModification(String tableName, Collection<String> keys) {
    this.tableName = tableName;
    this.keys.addAll(keys);
  }

  public final Map<String, Object> attributeValues = new LinkedHashMap<>();

  public void setValue(String attributeName, Object value) {
    keyUniqueness = null;
    attributeValues.put(attributeName.toUpperCase(), value);
  }

  public void checkAllKeyValuesExists() {
    for (String key : keys) {
      Object keyValue = attributeValues.get(key);
      if (keyValue == null) {
        throw new RuntimeException("No value for key " + key + " while changing table " + tableName
            + ", exists attributeNames: " + attributeValues.keySet());
      }
    }
  }

  private String keyUniqueness = null;

  public String keyUniqueness() {
    if (keyUniqueness != null) return keyUniqueness;

    List<String> attrList = new ArrayList<>();
    attrList.addAll(attributeValues.keySet());
    Collections.sort(attrList);

    StringBuilder sb = new StringBuilder(tableName);
    for (String attrName : attrList) {
      sb.append(' ').append(attrName);
    }

    return keyUniqueness = sb.toString();
  }

}
