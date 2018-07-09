package nf36_example_with_depinject.generated.impl.postgres.upsert.transaction;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.transaction.TransactionUpsert;

public class TransactionUpsertImpl implements TransactionUpsert {
  private final Nf36Upserter upserter;

  public TransactionUpsertImpl(Nf36Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.setNf3TableName("transaction");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id", id);
  }

  @Override
  public TransactionUpsert more(long id) {
    return new TransactionUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public TransactionUpsert description(String description) {
    upserter.putField("memory_never_be_superfluous.transaction_description", "description", description);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
