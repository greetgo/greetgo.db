package nf36_example_with_depinject.generated.faces;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;
import nf36_example_with_depinject.generated.faces.upsert.EntityWithManyIdsUpsert;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;

public interface ExampleUpserter {
  ChairUpsert chair(long id1, String id2);

  long chairNextId1();

  CharmUpsert charm(String id);

  ClientUpsert client(long id);

  long clientNextId();

  ClientAddressUpsert clientAddress(long clientId);

  long clientAddressNextClientId();

  EntityWithManyIdsUpsert entityWithManyIds(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId);

  int entityWithManyIdsNextIntId();

  Integer entityWithManyIdsNextBoxedIntId();

  long entityWithManyIdsNextLongId();

  Long entityWithManyIdsNextBoxedLongId();

  PersonUpsert person(String id);

  StoneUpsert stone(String id);

  StreetUpsert street(long id);

  long streetNextId();

  WowUpsert wow(String wowId, String wowId2);

}
