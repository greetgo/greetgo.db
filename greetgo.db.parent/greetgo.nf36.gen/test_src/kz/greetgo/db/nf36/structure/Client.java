package kz.greetgo.db.nf36.structure;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3NotNull;
import kz.greetgo.db.nf36.core.Nf3Text;

@Nf3Description("Это клиент")
@SuppressWarnings("unused")
public class Client {
  @Nf3Description("Идентификатор клиента")
  @Nf3ID(seqFrom = 10_000_000)
  public long id;

  @Nf3Description("Фамилия")
  @Nf3NotNull
  public String surname;

  @Nf3Description("Имя")
  public String name;

  @Nf3Description("Отчество")
  public String patronymic;

  @Nf3Description("Длинное описание")
  @Nf3Text
  public String longDescription;
}
