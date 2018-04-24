package kz.greetgo.db.nf36.gen.example.generated.impl;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleUpserter;
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
  public StreetUpsert street(long id) {
    return new StreetUpsertImpl(createUpserter(), id);
  }

  @Override
  public WowUpsert wow(String wowId, String wowId2) {
    return new WowUpsertImpl(createUpserter(), wowId, wowId2);
  }

}
