package nf36_example_with_depinject.generated.faces;

import nf36_example_with_depinject.generated.faces.save.ClientSave;
import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;
import nf36_example_with_depinject.generated.faces.upsert.EntityEnumAsIdUpsert;
import nf36_example_with_depinject.generated.faces.upsert.ManyIdsUpsert;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;
import nf36_example_with_depinject.generated.faces.upsert.transaction.OnlyIdsUpsert;
import nf36_example_with_depinject.generated.faces.upsert.transaction.TransactionUpsert;
import nf36_example_with_depinject.generated.faces.upsert.transaction.more.SchoolUpsert;
import nf36_example_with_depinject.structure.SomeEnum;

public interface ExampleUpserter {
  ChairUpsert chair(long id1, String id2);

  long chairNextId1();

  CharmUpsert charm(String id);

  ClientUpsert client(long id);

  ClientSave client();

  long clientNextId();

  ClientAddressUpsert clientAddress(long clientId);

  long clientAddressNextClientId();

  EntityEnumAsIdUpsert entityEnumAsId(SomeEnum id);

  ManyIdsUpsert manyIds(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId);

  int manyIdsNextIntId();

  Integer manyIdsNextBoxedIntId();

  long manyIdsNextLongId();

  Long manyIdsNextBoxedLongId();

  OnlyIdsUpsert onlyIds(long id1, String id2);

  long onlyIdsNextId1();

  PersonUpsert person(String id);

  SchoolUpsert school(String id);

  StoneUpsert stone(String id);

  StreetUpsert street(long id);

  long streetNextId();

  TransactionUpsert transaction(long id);

  long transactionNextId();

  WowUpsert wow(String wowId, String wowId2);

}
