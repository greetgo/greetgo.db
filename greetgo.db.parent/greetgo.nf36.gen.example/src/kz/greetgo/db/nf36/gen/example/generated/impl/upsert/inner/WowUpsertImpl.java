package kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;

public class WowUpsertImpl implements WowUpsert {
  private final Nf36Upserter upserter;

  public WowUpsertImpl(Nf36Upserter upserter, String wowId, String wowId2) {
    this.upserter = upserter;
    upserter.setNf3TableName("wow");
    upserter.setTimeFieldName("ts");
    upserter.putId("wow_id", wowId);
    upserter.putId("wow_id2", wowId2);
  }

  @Override
  public WowUpsert hello(String hello) {
    upserter.putField("m_wow_hello", "hello", hello);
    return this;
  }

  @Override
  public void commit() {
    upserter.commit();
  }
}
