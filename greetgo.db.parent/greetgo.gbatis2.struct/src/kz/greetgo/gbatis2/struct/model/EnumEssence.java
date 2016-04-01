package kz.greetgo.gbatis2.struct.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumEssence implements SimpleEssence {
  public final String fullClassName;

  public EnumEssence(String fullClassName) {
    this.fullClassName = fullClassName;
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "ENUM " + fullClassName;
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitEnum(this);
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
