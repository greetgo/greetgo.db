package nf36_example_with_depinject.sctructure2;

import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3ID;

@SuppressWarnings("unused")
@Nf3Entity
public class Transaction {
  @Nf3ID
  public long id;

  public String description;
}
