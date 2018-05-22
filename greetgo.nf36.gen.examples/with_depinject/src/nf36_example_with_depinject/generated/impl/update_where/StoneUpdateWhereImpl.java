package nf36_example_with_depinject.generated.impl.update_where;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.StoneUpdateWhere;

public class StoneUpdateWhereImpl implements StoneUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public StoneUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("stone");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id");
  }

  @Override
  public StoneUpdateWhere setName(String name) {
    this.whereUpdater.setField("memory_never_be_superfluous.stone_name", "name", name);
    return this;
  }

  @Override
  public StoneUpdateWhere setActual(boolean actual) {
    this.whereUpdater.setField("memory_never_be_superfluous.stone_actual", "actual", actual);
    return this;
  }



  @Override
  public StoneUpdateWhere whereActualIsEqualTo(boolean actual) {
    this.whereUpdater.where("actual", actual);
    return this;
  }

  @Override
  public StoneUpdateWhere whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Stone.id cannot be null");
    }
    this.whereUpdater.where("id", id);
    return this;
  }

  @Override
  public StoneUpdateWhere whereNameIsEqualTo(String name) {
    this.whereUpdater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
