package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.EnumAlreadyDefined;

import java.util.HashMap;
import java.util.Map;

public class Enums {
  private final Map<String, EnumDot> enumMap = new HashMap<>();

  public void append(EnumDot enumDot) {
    EnumDot alreadyExistsEnumDot = enumMap.get(enumDot.name);
    
    if (alreadyExistsEnumDot != null) {
      if (alreadyExistsEnumDot.enumClass.equals(enumDot.enumClass)) return;
      throw new EnumAlreadyDefined(enumDot.name, enumDot.place, alreadyExistsEnumDot.place);
    }

    enumMap.put(enumDot.name, enumDot);
  }
}
