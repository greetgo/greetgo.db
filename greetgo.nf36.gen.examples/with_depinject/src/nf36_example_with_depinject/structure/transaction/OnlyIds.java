package nf36_example_with_depinject.structure.transaction;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Entity
@Nf3Description("Using only ids")
public class OnlyIds {
  @Nf3ID
  @Nf3Description("id №1")
  public long id1;

  @Nf3ID(order = 2)
  @Nf3Description("id №2")
  public String id2;
}
