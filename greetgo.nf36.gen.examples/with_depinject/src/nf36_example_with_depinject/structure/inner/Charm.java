package nf36_example_with_depinject.structure.inner;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Description("Характер")
@SuppressWarnings("unused")
public class Charm {
  @Nf3Description("hi")
  @Nf3ID
  public String id;
  @Nf3Description("hi")
  public String name;
}
