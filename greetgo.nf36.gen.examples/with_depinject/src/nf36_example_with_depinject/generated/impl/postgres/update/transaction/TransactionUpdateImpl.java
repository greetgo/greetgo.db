package nf36_example_with_depinject.generated.impl.postgres.update.transaction;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import nf36_example_with_depinject.generated.faces.update.transaction.TransactionUpdate;

public class TransactionUpdateImpl implements TransactionUpdate {
  private final Nf36Updater updater;

  public TransactionUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("transaction");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public TransactionUpdate setDescription(String description) {
    this.updater.setField("memory_never_be_superfluous.transaction_description", "description", description);
    return this;
  }



  @Override
  public TransactionUpdate whereDescriptionIsEqualTo(String description) {
    this.updater.where("description", description);
    return this;
  }

  @Override
  public TransactionUpdate whereIdIsEqualTo(long id) {
    this.updater.where("id", id);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
