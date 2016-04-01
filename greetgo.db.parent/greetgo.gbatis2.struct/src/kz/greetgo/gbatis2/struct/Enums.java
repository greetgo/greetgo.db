package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.EnumAlreadyDefined;
import kz.greetgo.gbatis2.struct.model.EnumEssence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Enums {
  final Map<String, EnumDot> enumMap = new HashMap<>();

  public void append(EnumDot enumDot) {
    EnumDot alreadyExistsEnumDot = enumMap.get(enumDot.name);

    if (alreadyExistsEnumDot != null) {
      if (Objects.equals(alreadyExistsEnumDot.enumClassName, enumDot.enumClassName)) return;
      throw new EnumAlreadyDefined(enumDot.name, enumDot.place, alreadyExistsEnumDot.place);
    }

    enumMap.put(enumDot.name, enumDot);
  }

  public EnumEssence take(String name) {
    EnumDot enumDot = enumMap.get(name);
    if (enumDot == null) return null;
    return enumDot.essence();
  }
}
