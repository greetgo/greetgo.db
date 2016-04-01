package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;
import kz.greetgo.gbatis2.struct.model.SimpleEssence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StdFloat implements StdEssence {
  public static StdEssence INSTANCE = new StdFloat();

  private StdFloat() {
  }

  @Override
  public boolean isSequential() {
    return false;
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

  @Override
  public String toString() {
    return "float";
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitFloat(this);
  }
}
