package kz.greetgo.db.nf36.gen.example.structure;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Length;

@Nf3Description("person")
@SuppressWarnings("unused")
public class Person {
  @Nf3ID
  @Nf3Length(113)
  @Nf3Description("id")
  public String id;

  @Nf3Length(119)
  @Nf3Description("fio")
  public String fio;
}
