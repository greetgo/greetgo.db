package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

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
}
