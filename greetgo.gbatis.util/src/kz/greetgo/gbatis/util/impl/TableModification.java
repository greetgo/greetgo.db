package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.util.Getter;

import java.util.*;

public class TableModification {
  public final String tableName;
  private final Getter<String> objectInfo;
  public final List<String> keys = new ArrayList<>();

  public TableModification(String tableName, Collection<String> keys, Getter<String> objectInfo) {
    this.tableName = tableName;
    this.objectInfo = objectInfo;
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

  public String info() {
    if (objectInfo == null) return "Changing table " + tableName;
    return "Changing table " + tableName + " from " + objectInfo.get();
  }
}
