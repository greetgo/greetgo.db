package kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.CharmUpsert;

public class CharmUpsertImpl implements CharmUpsert {
  private final Nf36Upserter upserter;

  public CharmUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.putId("id", id);
  }

  @Override
  public CharmUpsert name(String name) {
    upserter.putField("name", name);
    return this;
  }

  @Override
  public void go() {
    upserter.setTableName("charm");
    upserter.setNf3Prefix("");
    upserter.setNf6Prefix("m_");
    upserter.go();
  }
}
