package kz.greetgo.db.nf36.gen.example.structure.inner;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Text;

@Nf3Description("Это стул")
@SuppressWarnings("unused")
public class Chair {
  @Nf3Description("Первый идентификатор стула")
  @Nf3ID(seqFrom = 10_000_000)
  public long id1;

  @Nf3Description("Второй идентификатор стула")
  @Nf3ID(order = 2)
  public String id2;

  @Nf3Description("Имя стула")
  public String name = "Мой стул";

  @Nf3Description("Описание стула")
  @Nf3Text
  public String description = "Длинное описание стула";
}
