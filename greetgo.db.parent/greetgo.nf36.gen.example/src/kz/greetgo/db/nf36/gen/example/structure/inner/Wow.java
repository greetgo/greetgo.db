package kz.greetgo.db.nf36.gen.example.structure.inner;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;

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
