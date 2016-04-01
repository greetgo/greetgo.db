package kz.greetgo.gbatis2.struct.model;

import kz.greetgo.gbatis2.struct.Place;
import kz.greetgo.gbatis2.struct.exceptions.EnumClassNotFound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumEssence implements SimpleEssence {
  public final String enumClassName;
  private final Place place;

  public EnumEssence(String enumClassName, Place place) {
    this.enumClassName = enumClassName;
    this.place = place;
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "ENUM " + enumClassName;
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitEnum(this);
  }

  private List<SimpleEssence> simpleEssenceListCache = null;

  @Override
  public List<SimpleEssence> keySimpleEssenceList() {
    if (simpleEssenceListCache == null) {
      List<SimpleEssence> ret = new ArrayList<>();
      ret.add(this);
      simpleEssenceListCache = Collections.unmodifiableList(ret);
    }

    return simpleEssenceListCache;
  }

  public static Class<? extends Enum> loadClass(String enumClassName, Place place) {
    try {
      //noinspection unchecked
      return (Class<? extends Enum>) Class.forName(enumClassName);
    } catch (ClassNotFoundException e) {
      throw new EnumClassNotFound(enumClassName, place, e);
    }
  }

  private Class<? extends Enum> enumClassCache = null;

  public Class<? extends Enum> enumClass() {
    if (enumClassCache == null) enumClassCache = loadClass(enumClassName, place);
    return enumClassCache;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EnumEssence that = (EnumEssence) o;

    return enumClassName != null ? enumClassName.equals(that.enumClassName) : that.enumClassName == null;

  }

  @Override
  public int hashCode() {
    return enumClassName != null ? enumClassName.hashCode() : 0;
  }
}
