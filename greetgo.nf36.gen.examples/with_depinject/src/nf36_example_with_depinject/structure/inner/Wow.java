package nf36_example_with_depinject.structure.inner;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Entity
@Nf3Description("wow")
@SuppressWarnings("unused")
public class Wow {
  @Nf3Description("hi")
  @Nf3ID
  public String wowId;

  @Nf3Description("hi")
  @Nf3ID(order = 2)
  public String wowId2;

  @Nf3Description("hi")
  public String hello;
}
