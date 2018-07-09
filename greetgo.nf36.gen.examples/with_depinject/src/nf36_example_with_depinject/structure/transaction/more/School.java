package nf36_example_with_depinject.structure.transaction.more;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Entity
@SuppressWarnings("unused")
@Nf3Description("Description")
public class School {

  @Nf3ID
  @Nf3Description("Description")
  public String id;

  @Nf3Description("Description")
  public String name;
}
