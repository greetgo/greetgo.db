package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;
import kz.greetgo.gbatis2.struct.model.SimpleEssence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StdStr implements StdEssence {
  public int length;

  public StdStr(int length) {
    this.length = length;
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "str" + length;
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitStr(this);
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StdStr stdStr = (StdStr) o;

    return length == stdStr.length;

  }

  @Override
  public int hashCode() {
    return length;
  }
}
