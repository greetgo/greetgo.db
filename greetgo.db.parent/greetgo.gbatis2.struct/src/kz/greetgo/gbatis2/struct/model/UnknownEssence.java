package kz.greetgo.gbatis2.struct.model;

public class UnknownEssence extends RuntimeException {
  public final Essence type;

  public UnknownEssence(Essence type) {
    super("" + type);
    this.type = type;
  }
}
