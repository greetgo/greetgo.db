package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.AliasAlreadyDefined;

import java.util.HashMap;
import java.util.Map;

public class Aliases {
  private final Map<String, AliasDot> aliasMap = new HashMap<>();

  public void append(AliasDot aliasDot) {
    AliasDot alreadyExistsAlias = aliasMap.get(aliasDot.name);
    if (alreadyExistsAlias != null) {
      if (alreadyExistsAlias.target.equals(aliasDot.target)) return;
      throw new AliasAlreadyDefined(aliasDot.name, aliasDot.place, alreadyExistsAlias.place);
    }

    aliasMap.put(aliasDot.name, aliasDot);

  }
}
