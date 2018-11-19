package nf36_example_with_depinject.structure.inner;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3Entity;
import kz.greetgo.db.nf36.core.Nf3GenerateHistorySelector;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Text;

@Nf3Entity
@Nf3Description("Это стул ' и апостроф")
@Nf3GenerateHistorySelector(toSuffix = "Into")
@SuppressWarnings("unused")
public class Chair {
  @Nf3ID(seqFrom = 10_000_000)
  @Nf3Description("Первый идентификатор стула")
  public long id1;

  @Nf3ID(order = 2)
  @Nf3Description("Второй идентификатор стула")
  public String id2;

  @Nf3Description("Имя стула")
  public String name = "Мой стул";

  @Nf3Text
  @Nf3Description("Описание стула")
  public String description = "Длинное описание стула";
}
