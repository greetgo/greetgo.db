package kz.greetgo.db.nf36.gen.example.structure.inner;

import kz.greetgo.db.nf36.core.Nf3ID;

@SuppressWarnings("unused")
public class Wow {
  @Nf3ID
  public String wowId;

  @Nf3ID(order = 2)
  public String wowId2;

  public String hello;
}
