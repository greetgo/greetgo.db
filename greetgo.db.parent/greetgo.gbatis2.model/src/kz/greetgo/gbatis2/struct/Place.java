package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.resource.ResourceRef;

public class Place {

  public final ResourceRef ref;
  public final int lineNumber;

  public Place(ResourceRef ref, int lineNumber) {
    this.ref = ref;
    this.lineNumber = lineNumber;
  }

  public String placement() {
    return ref.display() + ":" + lineNumber;
  }
}
