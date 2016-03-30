package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

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
}
