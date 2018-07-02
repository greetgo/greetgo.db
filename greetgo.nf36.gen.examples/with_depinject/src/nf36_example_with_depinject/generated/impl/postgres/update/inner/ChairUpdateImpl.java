package nf36_example_with_depinject.generated.impl.postgres.update.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.inner.ChairUpdate;

public class ChairUpdateImpl implements ChairUpdate {
  private final Nf36Updater updater;

  public ChairUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("chair");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id1", "id2");
  }

  @Override
  public ChairUpdate setName(String name) {
    this.updater.setField("memory_never_be_superfluous.chair_name", "name", name);
    return this;
  }

  @Override
  public ChairUpdate setDescription(String description) {
    this.updater.setField("memory_never_be_superfluous.chair_description", "description", description);
    return this;
  }



  @Override
  public ChairUpdate whereDescriptionIsEqualTo(String description) {
    this.updater.where("description", description);
    return this;
  }

  @Override
  public ChairUpdate whereId1IsEqualTo(long id1) {
    this.updater.where("id1", id1);
    return this;
  }

  @Override
  public ChairUpdate whereId2IsEqualTo(String id2) {
    if (id2 == null) {
      throw new CannotBeNull("Field Chair.id2 cannot be null");
    }
    this.updater.where("id2", id2);
    return this;
  }

  @Override
  public ChairUpdate whereNameIsEqualTo(String name) {
    this.updater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
