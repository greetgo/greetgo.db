package nf36_example_with_depinject.generated.impl.postgres;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SequenceNext;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
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
import nf36_example_with_depinject.generated.impl.postgres.save.ClientSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.EntityEnumAsIdSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.ManyIdsSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.PersonSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.StoneSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.StreetSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.inner.ChairSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.inner.CharmSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.inner.ClientAddressSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.inner.WowSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.transaction.OnlyIdsSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.transaction.TransactionSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.save.transaction.more.SchoolSaveImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.ClientUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.EntityEnumAsIdUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.ManyIdsUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.PersonUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.StoneUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.StreetUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.ChairUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.CharmUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.ClientAddressUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.WowUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.transaction.OnlyIdsUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.transaction.TransactionUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.transaction.more.SchoolUpsertImpl;
import nf36_example_with_depinject.structure.SomeEnum;

public abstract class AbstractExampleUpserterPostgres implements ExampleUpserter {
  protected abstract Nf36Upserter createUpserter();

  protected abstract Nf36Saver createSaver();

  protected abstract SequenceNext getSequenceNext();

  @Override
  public ChairUpsert chair(long id1, String id2) {
    return new ChairUpsertImpl(createUpserter(), id1, id2);
  }

  @Override
  public ChairSave chair() {
    return new ChairSaveImpl(createSaver());
  }

  @Override
  public long chairNextId1() {
    return getSequenceNext().nextLong("s_chair_id1");
  }

  @Override
  public CharmUpsert charm(String id) {
    return new CharmUpsertImpl(createUpserter(), id);
  }

  @Override
  public CharmSave charm() {
    return new CharmSaveImpl(createSaver());
  }

  @Override
  public ClientUpsert client(long id) {
    return new ClientUpsertImpl(createUpserter(), id);
  }

  @Override
  public ClientSave client() {
    return new ClientSaveImpl(createSaver());
  }

  @Override
  public long clientNextId() {
    return getSequenceNext().nextLong("s_client_id");
  }

  @Override
  public ClientAddressUpsert clientAddress(long clientId) {
    return new ClientAddressUpsertImpl(createUpserter(), clientId);
  }

  @Override
  public ClientAddressSave clientAddress() {
    return new ClientAddressSaveImpl(createSaver());
  }

  @Override
  public long clientAddressNextClientId() {
    return getSequenceNext().nextLong("s_client_address_client_id");
  }

  @Override
  public EntityEnumAsIdUpsert entityEnumAsId(SomeEnum id) {
    return new EntityEnumAsIdUpsertImpl(createUpserter(), id);
  }

  @Override
  public EntityEnumAsIdSave entityEnumAsId() {
    return new EntityEnumAsIdSaveImpl(createSaver());
  }

  @Override
  public ManyIdsUpsert manyIds(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    return new ManyIdsUpsertImpl(createUpserter(), intId, boxedIntId, longId, boxedLongId, strId);
  }

  @Override
  public ManyIdsSave manyIds() {
    return new ManyIdsSaveImpl(createSaver());
  }

  @Override
  public int manyIdsNextIntId() {
    return getSequenceNext().nextInt("s_many_ids_int_id");
  }

  @Override
  public Integer manyIdsNextBoxedIntId() {
    return getSequenceNext().nextInteger("s_many_ids_boxed_int_id");
  }

  @Override
  public long manyIdsNextLongId() {
    return getSequenceNext().nextLong("s_many_ids_long_id");
  }

  @Override
  public Long manyIdsNextBoxedLongId() {
    return getSequenceNext().nextLong("s_many_ids_boxed_long_id");
  }

  @Override
  public OnlyIdsUpsert onlyIds(long id1, String id2) {
    return new OnlyIdsUpsertImpl(createUpserter(), id1, id2);
  }

  @Override
  public OnlyIdsSave onlyIds() {
    return new OnlyIdsSaveImpl(createSaver());
  }

  @Override
  public long onlyIdsNextId1() {
    return getSequenceNext().nextLong("s_only_ids_id1");
  }

  @Override
  public PersonUpsert person(String id) {
    return new PersonUpsertImpl(createUpserter(), id);
  }

  @Override
  public PersonSave person() {
    return new PersonSaveImpl(createSaver());
  }

  @Override
  public SchoolUpsert school(String id) {
    return new SchoolUpsertImpl(createUpserter(), id);
  }

  @Override
  public SchoolSave school() {
    return new SchoolSaveImpl(createSaver());
  }

  @Override
  public StoneUpsert stone(String id) {
    return new StoneUpsertImpl(createUpserter(), id);
  }

  @Override
  public StoneSave stone() {
    return new StoneSaveImpl(createSaver());
  }

  @Override
  public StreetUpsert street(long id) {
    return new StreetUpsertImpl(createUpserter(), id);
  }

  @Override
  public StreetSave street() {
    return new StreetSaveImpl(createSaver());
  }

  @Override
  public long streetNextId() {
    return getSequenceNext().nextLong("s_street_id");
  }

  @Override
  public TransactionUpsert transaction(long id) {
    return new TransactionUpsertImpl(createUpserter(), id);
  }

  @Override
  public TransactionSave transaction() {
    return new TransactionSaveImpl(createSaver());
  }

  @Override
  public long transactionNextId() {
    return getSequenceNext().nextLong("s_transaction_id");
  }

  @Override
  public WowUpsert wow(String wowId, String wowId2) {
    return new WowUpsertImpl(createUpserter(), wowId, wowId2);
  }

  @Override
  public WowSave wow() {
    return new WowSaveImpl(createSaver());
  }

}
