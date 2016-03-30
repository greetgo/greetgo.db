package kz.greetgo.gbatis2.struct.model;

public interface SimpleEssence extends Essence {
  <T> T visit(EssenceVisitor<T> visitor);
}
