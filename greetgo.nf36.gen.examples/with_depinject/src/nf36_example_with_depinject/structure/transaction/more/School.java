package nf36_example_with_depinject.structure.transaction.more;

import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3ID;

@SuppressWarnings("unused")
@Nf3Entity
public class School {
  @Nf3ID
  public String id;

  public String name;
}
