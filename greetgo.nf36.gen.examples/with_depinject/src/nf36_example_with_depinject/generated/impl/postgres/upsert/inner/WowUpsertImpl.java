package nf36_example_with_depinject.generated.impl.postgres.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;

public class WowUpsertImpl implements WowUpsert {
  private final Nf36Upserter upserter;

  public WowUpsertImpl(Nf36Upserter upserter, String wowId, String wowId2) {
    this.upserter = upserter;
    upserter.setNf3TableName("wow");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("wow_id", wowId);
    upserter.putId("wow_id2", wowId2);
  }

  @Override
  public WowUpsert more(String wowId, String wowId2) {
    return new WowUpsertImpl(this.upserter.more(), wowId, wowId2);
  }

  @Override
  public WowUpsert hello(String hello) {
    upserter.putField("memory_never_be_superfluous.wow_hello", "hello", hello);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
