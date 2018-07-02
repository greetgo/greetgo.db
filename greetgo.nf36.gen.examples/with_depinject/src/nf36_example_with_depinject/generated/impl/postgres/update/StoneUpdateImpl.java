package nf36_example_with_depinject.generated.impl.postgres.update;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.StoneUpdate;

public class StoneUpdateImpl implements StoneUpdate {
  private final Nf36Updater updater;

  public StoneUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("stone");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public StoneUpdate setName(String name) {
    this.updater.setField("memory_never_be_superfluous.stone_name", "name", name);
    return this;
  }

  @Override
  public StoneUpdate setActual(boolean actual) {
    this.updater.setField("memory_never_be_superfluous.stone_actual", "actual", actual);
    return this;
  }



  @Override
  public StoneUpdate whereActualIsEqualTo(boolean actual) {
    this.updater.where("actual", actual);
    return this;
  }

  @Override
  public StoneUpdate whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Stone.id cannot be null");
    }
    this.updater.where("id", id);
    return this;
  }

  @Override
  public StoneUpdate whereNameIsEqualTo(String name) {
    this.updater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
