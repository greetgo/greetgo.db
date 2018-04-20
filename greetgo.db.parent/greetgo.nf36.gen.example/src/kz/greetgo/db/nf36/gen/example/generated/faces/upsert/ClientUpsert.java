package kz.greetgo.db.nf36.gen.example.generated.faces.upsert;

import java.lang.Long;
import java.lang.String;

public interface ClientUpsert {
  ClientUpsert surname(String surname);

  ClientUpsert name(String name);

  ClientUpsert patronymic(String patronymic);

  ClientUpsert longDescription(String longDescription);

  ClientUpsert myChairId1(Long myChairId1);

  ClientUpsert myChairId2(String myChairId2);

  ClientUpsert hisChairLongId(Long hisChairLongId);

  ClientUpsert hisChairStrId(String hisChairStrId);

  void go();
}
