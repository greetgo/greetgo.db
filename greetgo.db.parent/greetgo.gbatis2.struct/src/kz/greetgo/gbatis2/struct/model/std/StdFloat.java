package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

public class StdFloat implements StdEssence {
  public static StdEssence INSTANCE = new StdFloat();

  private StdFloat() {
  }

  @Override
  public boolean isSequential() {
    return false;
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
