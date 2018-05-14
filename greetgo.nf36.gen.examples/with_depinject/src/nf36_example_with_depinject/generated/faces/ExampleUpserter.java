package nf36_example_with_depinject.generated.faces;

import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;

public interface ExampleUpserter {
  ChairUpsert chair(long id1, String id2);

  CharmUpsert charm(String id);

  ClientUpsert client(long id);

  ClientAddressUpsert clientAddress(long clientId);

  PersonUpsert person(String id);

  StreetUpsert street(long id);

  WowUpsert wow(String wowId, String wowId2);

}
