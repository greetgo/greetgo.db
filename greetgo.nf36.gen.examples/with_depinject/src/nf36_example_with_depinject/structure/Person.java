package nf36_example_with_depinject.structure;

import kz.greetgo.db.nf36.core.Nf3DefaultValue;
import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Length;

import java.math.BigDecimal;

@Nf3Description("person")
@SuppressWarnings("unused")
public class Person {
  @Nf3ID
  @Nf3Length(113)
  @Nf3Description("id")
  public String id;

  @Nf3Length(119)
  @Nf3Description("fio")
  @Nf3DefaultValue("Вселенское ' значение")
  public String fio;

  @Nf3Description("blocked")
  public boolean blocked = true;

  @Nf3Description("amount")
  public BigDecimal amount;
}
