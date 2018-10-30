package nf36_example_with_depinject.generated.faces;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import nf36_example_with_depinject.generated.faces.save.ClientSave;
import nf36_example_with_depinject.generated.faces.save.EntityEnumAsIdSave;
import nf36_example_with_depinject.generated.faces.save.ManyIdsSave;
import nf36_example_with_depinject.generated.faces.save.PersonSave;
import nf36_example_with_depinject.generated.faces.save.StoneSave;
import nf36_example_with_depinject.generated.faces.save.StreetSave;
import nf36_example_with_depinject.generated.faces.save.inner.ChairSave;
import nf36_example_with_depinject.generated.faces.save.inner.CharmSave;
import nf36_example_with_depinject.generated.faces.save.inner.ClientAddressSave;
import nf36_example_with_depinject.generated.faces.save.inner.WowSave;
import nf36_example_with_depinject.generated.faces.save.transaction.OnlyIdsSave;
import nf36_example_with_depinject.generated.faces.save.transaction.TransactionSave;
import nf36_example_with_depinject.generated.faces.save.transaction.more.SchoolSave;
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

  ChairSave chair();

  long chairNextId1();

  CharmUpsert charm(String id);

  CharmSave charm();

  ClientUpsert client(long id);

  ClientSave client();

  long clientNextId();

  ClientAddressUpsert clientAddress(long clientId);

  ClientAddressSave clientAddress();

  long clientAddressNextClientId();

  EntityEnumAsIdUpsert entityEnumAsId(SomeEnum id);

  EntityEnumAsIdSave entityEnumAsId();

  ManyIdsUpsert manyIds(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId);

  ManyIdsSave manyIds();

  int manyIdsNextIntId();

  Integer manyIdsNextBoxedIntId();

  long manyIdsNextLongId();

  Long manyIdsNextBoxedLongId();

  OnlyIdsUpsert onlyIds(long id1, String id2);

  OnlyIdsSave onlyIds();

  long onlyIdsNextId1();

  PersonUpsert person(String id);

  PersonSave person();

  SchoolUpsert school(String id);

  SchoolSave school();

  StoneUpsert stone(String id);

  StoneSave stone();

  StreetUpsert street(long id);

  StreetSave street();

  long streetNextId();

  TransactionUpsert transaction(long id);

  TransactionSave transaction();

  long transactionNextId();

  WowUpsert wow(String wowId, String wowId2);

  WowSave wow();

}
