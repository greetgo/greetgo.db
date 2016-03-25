package kz.greetgo.gbatis2.struct.exceptions;

import kz.greetgo.gbatis2.struct.AliasDot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AliasSetContainsCircling extends GbatisException {
  public AliasSetContainsCircling(Collection<AliasDot> aliasDots) {
    super(generateMessage(aliasDots));
    this.aliasDots.addAll(aliasDots);
  }

  private static String generateMessage(Collection<AliasDot> aliasDots) {
    StringBuilder sb = new StringBuilder();
    for (AliasDot aliasDot : aliasDots) {
      sb.append("\n\t\t");
      sb.append(aliasDot.display());
    }
    return sb.toString();
  }

  private final List<AliasDot> aliasDots = new ArrayList<>();

  public List<AliasDot> aliasDots() {
    return Collections.unmodifiableList(aliasDots);
  }
}
