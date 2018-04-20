package kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;

public class WowUpsertImpl implements WowUpsert {
  private final Upserter upserter;

  public WowUpsertImpl(Upserter upserter, String wowId) {
    this.upserter = upserter;
    upserter.putId("wow_id", wowId);
  }

  @Override
  public WowUpsert hello(String hello) {
    upserter.putField("hello", hello);
    return this;
  }

  @Override
  public void go() {
    upserter.setTableName("wow");
    upserter.setNf3Prefix("");
    upserter.setNf6Prefix("m_");
    upserter.go();
  }
}
