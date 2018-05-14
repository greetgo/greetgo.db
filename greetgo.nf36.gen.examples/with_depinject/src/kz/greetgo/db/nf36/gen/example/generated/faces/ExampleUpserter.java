package kz.greetgo.db.nf36.gen.example.generated.faces;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.PersonUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.StreetUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ChairUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.CharmUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ClientAddressUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;

public interface ExampleUpserter {
  ChairUpsert chair(long id1, String id2);

  CharmUpsert charm(String id);

  ClientUpsert client(long id);

  ClientAddressUpsert clientAddress(long clientId);

  PersonUpsert person(String id);

  StreetUpsert street(long id);

  WowUpsert wow(String wowId, String wowId2);

}
