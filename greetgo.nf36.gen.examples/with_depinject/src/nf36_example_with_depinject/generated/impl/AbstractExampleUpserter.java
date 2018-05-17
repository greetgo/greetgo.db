package nf36_example_with_depinject.generated.impl;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;
import nf36_example_with_depinject.generated.impl.upsert.ClientUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.PersonUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.StoneUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.StreetUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.inner.ChairUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.inner.CharmUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.inner.ClientAddressUpsertImpl;
import nf36_example_with_depinject.generated.impl.upsert.inner.WowUpsertImpl;

public abstract class AbstractExampleUpserter implements ExampleUpserter {
  protected abstract Nf36Upserter createUpserter();

  @Override
  public ChairUpsert chair(long id1, String id2) {
    return new ChairUpsertImpl(createUpserter(), id1, id2);
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
  public ClientAddressUpsert clientAddress(long clientId) {
    return new ClientAddressUpsertImpl(createUpserter(), clientId);
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
  public WowUpsert wow(String wowId, String wowId2) {
    return new WowUpsertImpl(createUpserter(), wowId, wowId2);
  }

}
