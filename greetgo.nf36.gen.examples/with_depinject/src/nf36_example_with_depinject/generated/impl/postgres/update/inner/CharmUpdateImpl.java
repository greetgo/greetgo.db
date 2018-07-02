package nf36_example_with_depinject.generated.impl.postgres.update.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.inner.CharmUpdate;

public class CharmUpdateImpl implements CharmUpdate {
  private final Nf36Updater updater;

  public CharmUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("charm");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public CharmUpdate setName(String name) {
    this.updater.setField("memory_never_be_superfluous.charm_name", "name", name);
    return this;
  }



  @Override
  public CharmUpdate whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Charm.id cannot be null");
    }
    this.updater.where("id", id);
    return this;
  }

  @Override
  public CharmUpdate whereNameIsEqualTo(String name) {
    this.updater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
