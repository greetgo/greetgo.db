package nf36_example_with_depinject.structure;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;

@Nf3Description("Stone is rock")
public class Stone {

  @Nf3Description("id of stone")
  @Nf3ID
  public String id;

  @Nf3Description("name of stone")
  public String name;

  @Nf3Description("actuality of stone")
  public boolean actual;
}
