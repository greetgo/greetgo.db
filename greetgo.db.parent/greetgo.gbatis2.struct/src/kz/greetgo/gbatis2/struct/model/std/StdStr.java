package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

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
}
