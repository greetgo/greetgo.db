package nf36_example_with_depinject.generated.impl.oracle.update.transaction;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.transaction.OnlyIdsUpdate;

public class OnlyIdsUpdateImpl implements OnlyIdsUpdate {
  private final Nf36Updater updater;

  public OnlyIdsUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("only_ids");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id1", "id2");
  }



  @Override
  public OnlyIdsUpdate whereId1IsEqualTo(long id1) {
    this.updater.where("id1", id1);
    return this;
  }

  @Override
  public OnlyIdsUpdate whereId2IsEqualTo(String id2) {
    if (id2 == null) {
      throw new CannotBeNull("Field OnlyIds.id2 cannot be null");
    }
    this.updater.where("id2", id2);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
