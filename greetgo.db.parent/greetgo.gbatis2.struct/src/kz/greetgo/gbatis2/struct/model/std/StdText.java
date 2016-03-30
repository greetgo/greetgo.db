package kz.greetgo.gbatis2.struct.model.std;

import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

public class StdText implements StdEssence {
  public static StdEssence INSTANCE = new StdText();

  private StdText() {
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "text";
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitText(this);
  }
}
