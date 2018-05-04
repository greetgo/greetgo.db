package kz.greetgo.db.nf36.gen.example.structure;

import kz.greetgo.db.nf36.core.Nf3CommitMethodName;
import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.core.Nf3ID;
import kz.greetgo.db.nf36.core.Nf3MoreMethodName;
import kz.greetgo.db.nf36.core.Nf3NotNull;
import kz.greetgo.db.nf36.core.Nf3ReferenceTo;
import kz.greetgo.db.nf36.core.Nf3Text;
import kz.greetgo.db.nf36.gen.example.structure.inner.Chair;
import kz.greetgo.db.nf36.gen.example.structure.inner.Charm;

@Nf3Description("Это клиент")
@Nf3CommitMethodName("commitAll")
@Nf3MoreMethodName("moreAnother")
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

  @Nf3ReferenceTo(Charm.class)
  @Nf3Description("hi")
  public String charmId;

  @Nf3Description("Длинное описание")
  @Nf3Text
  public String longDescription;

  @Nf3Description("Ссылка на мой стул")
  @Nf3ReferenceTo(value = Chair.class, nextPart = "myChairId2")
  public Long myChairId1;

  @Nf3ReferenceTo(Chair.class)
  @Nf3Description("hi")
  public String myChairId2;

  @Nf3Description("Ссылка на его стул")
  @Nf3ReferenceTo(value = Chair.class, nextPart = "hisChairStrId")
  public Long hisChairLongId;

  @Nf3ReferenceTo(Chair.class)
  @Nf3Description("hi")
  public String hisChairStrId;
}
