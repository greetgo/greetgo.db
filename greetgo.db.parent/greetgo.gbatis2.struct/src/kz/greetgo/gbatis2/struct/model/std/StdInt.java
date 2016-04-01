package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;
import kz.greetgo.gbatis2.struct.model.SimpleEssence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StdInt implements StdEssence {
  public static StdEssence INSTANCE = new StdInt();

  private StdInt() {
  }

  @Override
  public boolean isSequential() {
    return true;
  }
  @Override
  public String toString() {
    return "int";
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitInt(this);
  }

  private List<SimpleEssence> simpleEssenceListCache = null;

  @Override
  public List<SimpleEssence> simpleEssenceList() {
    if (simpleEssenceListCache == null) {
      List<SimpleEssence> ret = new ArrayList<>();
      ret.add(this);
      simpleEssenceListCache = Collections.unmodifiableList(ret);
    }

    return simpleEssenceListCache;
  }
}
