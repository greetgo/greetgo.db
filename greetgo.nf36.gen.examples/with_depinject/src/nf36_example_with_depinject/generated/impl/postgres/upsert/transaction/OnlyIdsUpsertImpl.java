package nf36_example_with_depinject.generated.impl.postgres.upsert.transaction;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.transaction.OnlyIdsUpsert;

public class OnlyIdsUpsertImpl implements OnlyIdsUpsert {
  private final Nf36Upserter upserter;

  public OnlyIdsUpsertImpl(Nf36Upserter upserter, long id1, String id2) {
    this.upserter = upserter;
    upserter.setNf3TableName("only_ids");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id1", id1);
    upserter.putId("id2", id2);
  }

  @Override
  public OnlyIdsUpsert more(long id1, String id2) {
    return new OnlyIdsUpsertImpl(this.upserter.more(), id1, id2);
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
