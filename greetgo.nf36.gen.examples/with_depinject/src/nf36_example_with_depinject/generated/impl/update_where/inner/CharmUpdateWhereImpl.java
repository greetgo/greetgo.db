package nf36_example_with_depinject.generated.impl.update_where.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.inner.CharmUpdateWhere;

public class CharmUpdateWhereImpl implements CharmUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public CharmUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("charm");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id");
  }

  @Override
  public CharmUpdateWhere setName(String name) {
    this.whereUpdater.setField("memory_never_be_superfluous.charm_name", "name", name);
    return this;
  }



  @Override
  public CharmUpdateWhere whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Charm.id cannot be null");
    }
    this.whereUpdater.where("id", id);
    return this;
  }

  @Override
  public CharmUpdateWhere whereNameIsEqualTo(String name) {
    this.whereUpdater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
