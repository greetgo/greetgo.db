package kz.greetgo.db.nf36.gen.example.generated.impl;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleNf3Door;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.StreetUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ChairUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.CharmUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ClientAddressUpsert;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.ClientUpsertImpl;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.StreetUpsertImpl;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner.ChairUpsertImpl;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner.CharmUpsertImpl;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner.ClientAddressUpsertImpl;
import kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner.WowUpsertImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class AbstractExampleNf3Door implements ExampleNf3Door {
  protected abstract Nf36Upserter createUpserter();

  @Override
  public ClientUpsert upsertClient(long id) {
    return new ClientUpsertImpl(createUpserter(), id);
  }

  @Override
  public ClientAddressUpsert upsertClientAddress(long clientId) {
    return new ClientAddressUpsertImpl(createUpserter(), clientId);
  }

  @Override
  public StreetUpsert upsertStreet(long id) {
    return new StreetUpsertImpl(createUpserter(), id);
  }

  @Override
  public ChairUpsert upsertChair(long id1, String id2) {
    return new ChairUpsertImpl(createUpserter(), id1, id2);
  }

  @Override
  public WowUpsert upsertWow(String wowId, String wowId2) {
    return new WowUpsertImpl(createUpserter(), wowId, wowId2);
  }

  @Override
  public CharmUpsert upsertCharm(String id) {
    return new CharmUpsertImpl(createUpserter(), id);
  }

}
