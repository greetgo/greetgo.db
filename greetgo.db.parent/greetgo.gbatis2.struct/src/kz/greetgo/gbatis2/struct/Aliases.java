package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.AliasAlreadyDefined;
import kz.greetgo.gbatis2.struct.exceptions.AliasSetContainsCircling;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Aliases {
  final Map<String, AliasDot> aliasMap = new HashMap<>();

  public void append(AliasDot aliasDot) {
    AliasDot alreadyExistsAlias = aliasMap.get(aliasDot.name);
    if (alreadyExistsAlias != null) {
      if (alreadyExistsAlias.target.equals(aliasDot.target)) return;
      throw new AliasAlreadyDefined(aliasDot.name, aliasDot.place, alreadyExistsAlias.place);
    }

    aliasMap.put(aliasDot.name, aliasDot);
  }

  public String real(String name) {
    LinkedHashMap<String, AliasDot> circlingTracer = new LinkedHashMap<>();

    while (true) {
      if (circlingTracer.containsKey(name)) {
        throw new AliasSetContainsCircling(circlingTracer.values());
      }

      AliasDot aliasDot = aliasMap.get(name);
      if (aliasDot == null) return name;

      circlingTracer.put(name, aliasDot);
      name = aliasDot.target;
    }
  }
}
