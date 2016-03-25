package kz.greetgo.gbatis2.struct;

public class TestStrPlace implements Place {
  private String placement;

  public TestStrPlace(String placement) {
    this.placement = placement;
  }

  @Override
  public String placement() {
    return placement;
  }
}
