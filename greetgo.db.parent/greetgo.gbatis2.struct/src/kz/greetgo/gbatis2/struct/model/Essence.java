package kz.greetgo.gbatis2.struct.model;

import java.util.List;

public interface Essence {
  boolean isSequential();

  List<SimpleEssence> keySimpleEssenceList();
}
