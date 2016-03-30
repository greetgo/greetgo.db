package kz.greetgo.gbatis2.struct.model;

public class EnumEssence implements SimpleEssence {
  public final Class<? extends Enum> source;

  public EnumEssence(Class<? extends Enum> source) {
    this.source = source;
  }

  @Override
  public boolean isSequential() {
    return false;
  }

  @Override
  public String toString() {
    return "ENUM " + source.getName();
  }

  @Override
  public <T> T visit(EssenceVisitor<T> visitor) {
    return visitor.visitEnum(this);
  }
}
