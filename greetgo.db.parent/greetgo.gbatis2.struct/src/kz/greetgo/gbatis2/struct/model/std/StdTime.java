package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

public class StdTime implements StdEssence {
  public static StdEssence INSTANCE = new StdTime();

  private StdTime() {
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "time";
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitTime(this);
  }
}
