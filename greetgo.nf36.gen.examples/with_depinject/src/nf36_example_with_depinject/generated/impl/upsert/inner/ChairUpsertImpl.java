package nf36_example_with_depinject.generated.impl.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;

public class ChairUpsertImpl implements ChairUpsert {
  private final Nf36Upserter upserter;

  public ChairUpsertImpl(Nf36Upserter upserter, long id1, String id2) {
    this.upserter = upserter;
    upserter.setNf3TableName("chair");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id1", id1);
    upserter.putId("id2", id2);
  }

  public ChairUpsert more(long id1, String id2) {
    return new ChairUpsertImpl(this.upserter.more(), id1, id2);
  }

  @Override
  public ChairUpsert name(String name) {
    upserter.putField("memory_never_be_superfluous.chair_name", "name", name);
    return this;
  }

  @Override
  public ChairUpsert description(String description) {
    upserter.putField("memory_never_be_superfluous.chair_description", "description", description);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
