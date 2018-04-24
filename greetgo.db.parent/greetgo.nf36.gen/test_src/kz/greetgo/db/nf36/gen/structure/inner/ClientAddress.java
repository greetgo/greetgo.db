package kz.greetgo.db.nf36.gen.structure.inner;

import kz.greetgo.db.nf36.core.Nf3DefaultNow;
import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3NotNull;
import kz.greetgo.db.nf36.core.Nf3ReferenceTo;
import kz.greetgo.db.nf36.core.Nf3Short;
import kz.greetgo.db.nf36.gen.structure.AddressType;
import kz.greetgo.db.nf36.gen.structure.Client;
import kz.greetgo.db.nf36.gen.structure.Street;

import java.util.Date;

@Nf3Description("Адрес клиента")
@SuppressWarnings("unused")
public class ClientAddress {

  @Nf3Description("Ссылка на клиента")
  @Nf3ID(ref = Client.class)
  public long clientId;

  @Nf3Description("Тип адреса")
  public AddressType type;

  @Nf3Description("Улица")
  @Nf3ReferenceTo(Street.class)
  public long streetId;

  @Nf3Description("Дом")
  @Nf3Short
  public String house;

  @Nf3Description("Квартира")
  @Nf3Short
  public String flat;

  @Nf3Description("День рождения")
  @Nf3NotNull
  @Nf3DefaultNow
  public Date birthDate;
}
