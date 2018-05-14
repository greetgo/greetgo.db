package kz.greetgo.db.nf36.gen.example.generated.faces.upsert;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;

public interface ClientUpsert {
  ClientUpsert surname(String surname);

  ClientUpsert name(String name);

  ClientUpsert patronymic(String patronymic);

  ClientUpsert charmId(String charmId);

  ClientUpsert longDescription(String longDescription);

  ClientUpsert myChairId1(Long myChairId1);

  ClientUpsert myChairId2(String myChairId2);

  ClientUpsert hisChairLongId(Long hisChairLongId);

  ClientUpsert hisChairStrId(String hisChairStrId);

  ClientUpsert moreAnother(long id);

  void commitAll();
}
