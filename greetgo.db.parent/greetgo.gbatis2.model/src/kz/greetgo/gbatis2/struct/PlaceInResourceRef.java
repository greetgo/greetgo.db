package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.resource.ResourceRef;

public class PlaceInResourceRef implements Place {

  public final ResourceRef ref;
  public final int lineNumber;

  public PlaceInResourceRef(ResourceRef ref, int lineNumber) {
    this.ref = ref;
    this.lineNumber = lineNumber;
  }

  @Override
  public String placement() {
    return ref.display() + ":" + lineNumber;
  }
}
