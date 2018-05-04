package kz.greetgo.db.nf36.gen.example.structure;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3Ignore;
import kz.greetgo.db.nf36.core.Nf3NotNull;

@Nf3Description("Справочник улиц")
@SuppressWarnings("unused")
public class Street {

  @Nf3Description("Идентификатор улицы")
  @Nf3ID
  public long id;

  @Nf3Description("Тип улицы")
  @Nf3NotNull
  public StreetType type = StreetType.STREET;

  @Nf3Description("Имя")
  @Nf3NotNull
  public String name;

  @Nf3Ignore
  public String createdBy;
}
