package nf36_example_with_depinject.structure;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Description("Это энтити с большим количеством ИД-шников")
public class EntityWithManyIds {
  @Nf3ID(order = 1)
  public int intId;

  @Nf3ID(order = 2)
  public Integer boxedIntId;

  @Nf3ID(order = 3)
  public long longId;

  @Nf3ID(order = 4)
  public Long boxedLongId;

  @Nf3ID(order = 5)
  public String strId;

  public int aField;
}
