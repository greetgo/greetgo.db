package nf36_example_with_depinject.generated.impl.upsert;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;

public class StoneUpsertImpl implements StoneUpsert {
  private final Nf36Upserter upserter;

  public StoneUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.setNf3TableName("stone");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id", id);
  }

  public StoneUpsert more(String id) {
    return new StoneUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public StoneUpsert name(String name) {
    upserter.putField("memory_never_be_superfluous.stone_name", "name", name);
    return this;
  }

  @Override
  public StoneUpsert actual(boolean actual) {
    upserter.putField("memory_never_be_superfluous.stone_actual", "actual", actual);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
