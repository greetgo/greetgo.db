package nf36_example_with_depinject.generated.impl.postgres.update.transaction.more;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.transaction.more.SchoolUpdate;

public class SchoolUpdateImpl implements SchoolUpdate {
  private final Nf36Updater updater;

  public SchoolUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("school");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public SchoolUpdate setName(String name) {
    this.updater.setField("memory_never_be_superfluous.school_name", "name", name);
    return this;
  }



  @Override
  public SchoolUpdate whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field School.id cannot be null");
    }
    this.updater.where("id", id);
    return this;
  }

  @Override
  public SchoolUpdate whereNameIsEqualTo(String name) {
    this.updater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
