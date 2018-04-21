package kz.greetgo.db.nf36.gen.example.generated.faces;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.StreetUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ChairUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.CharmUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ClientAddressUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;

public interface TestNf3Door {
  ClientUpsert upsertClient(long id);

  ClientAddressUpsert upsertClientAddress(long clientId);

  StreetUpsert upsertStreet(long id);

  ChairUpsert upsertChair(long id1, String id2);

  WowUpsert upsertWow(String wowId, String wowId2);

  CharmUpsert upsertCharm(String id);

}
