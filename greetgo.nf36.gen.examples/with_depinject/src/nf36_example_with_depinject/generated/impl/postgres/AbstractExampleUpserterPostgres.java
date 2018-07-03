package nf36_example_with_depinject.generated.impl.postgres;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SequenceNext;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;
import nf36_example_with_depinject.generated.faces.upsert.EntityWithManyIdsUpsert;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;
import nf36_example_with_depinject.generated.impl.postgres.upsert.ClientUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.EntityWithManyIdsUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.PersonUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.StoneUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.StreetUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.ChairUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.CharmUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.ClientAddressUpsertImpl;
import nf36_example_with_depinject.generated.impl.postgres.upsert.inner.WowUpsertImpl;

public abstract class AbstractExampleUpserterPostgres implements ExampleUpserter {
  protected abstract Nf36Upserter createUpserter();

  protected abstract SequenceNext getSequenceNext();

  @Override
  public ChairUpsert chair(long id1, String id2) {
    return new ChairUpsertImpl(createUpserter(), id1, id2);
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
  public ClientUpsert client(long id) {
    return new ClientUpsertImpl(createUpserter(), id);
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
  public long clientAddressNextClientId() {
    return getSequenceNext().nextLong("s_client_address_client_id");
  }

  @Override
  public EntityWithManyIdsUpsert entityWithManyIds(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    return new EntityWithManyIdsUpsertImpl(createUpserter(), intId, boxedIntId, longId, boxedLongId, strId);
  }

  @Override
  public int entityWithManyIdsNextIntId() {
    return getSequenceNext().nextInt("s_entity_with_many_ids_int_id");
  }

  @Override
  public Integer entityWithManyIdsNextBoxedIntId() {
    return getSequenceNext().nextInteger("s_entity_with_many_ids_boxed_int_id");
  }

  @Override
  public long entityWithManyIdsNextLongId() {
    return getSequenceNext().nextLong("s_entity_with_many_ids_long_id");
  }

  @Override
  public Long entityWithManyIdsNextBoxedLongId() {
    return getSequenceNext().nextLong("s_entity_with_many_ids_boxed_long_id");
  }

  @Override
  public PersonUpsert person(String id) {
    return new PersonUpsertImpl(createUpserter(), id);
  }

  @Override
  public StoneUpsert stone(String id) {
    return new StoneUpsertImpl(createUpserter(), id);
  }

  @Override
  public StreetUpsert street(long id) {
    return new StreetUpsertImpl(createUpserter(), id);
  }

  @Override
  public long streetNextId() {
    return getSequenceNext().nextLong("s_street_id");
  }

  @Override
  public WowUpsert wow(String wowId, String wowId2) {
    return new WowUpsertImpl(createUpserter(), wowId, wowId2);
  }

}
