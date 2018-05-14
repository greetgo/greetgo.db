package nf36_example_with_depinject.generated.impl.upsert.inner;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.inner.CharmUpsert;

public class CharmUpsertImpl implements CharmUpsert {
  private final Nf36Upserter upserter;

  public CharmUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.setNf3TableName("charm");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.setTimeFieldName("ts");
    upserter.putId("id", id);
  }

  public CharmUpsert more(String id) {
    return new CharmUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public CharmUpsert name(String name) {
    upserter.putField("m_charm_name", "name", name);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
