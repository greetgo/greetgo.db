package kz.greetgo.gbatis2.struct;

public class AliasDot {
  public final String name, target;
  public final Place place;

  public AliasDot(String name, String target, Place place) {
    this.name = name;
    this.target = target;
    this.place = place;
  }

  public String display() {
    return name + " " + target + " at " + place.placement();
  }
}
