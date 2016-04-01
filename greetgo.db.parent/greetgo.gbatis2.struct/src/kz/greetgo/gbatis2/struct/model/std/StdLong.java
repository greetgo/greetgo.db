package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;
import kz.greetgo.gbatis2.struct.model.SimpleEssence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StdLong implements StdEssence {
  public static StdEssence INSTANCE = new StdLong();

  private StdLong() {
  }

  @Override
  public boolean isSequential() {
    return true;
  }

  @Override
  public String toString() {
    return "long";
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitLong(this);
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
