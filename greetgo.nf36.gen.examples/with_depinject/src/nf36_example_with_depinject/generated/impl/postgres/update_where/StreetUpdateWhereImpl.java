package nf36_example_with_depinject.generated.impl.postgres.update_where;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.StreetUpdateWhere;
import nf36_example_with_depinject.structure.StreetType;

public class StreetUpdateWhereImpl implements StreetUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public StreetUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("street");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id");
  }

  @Override
  public StreetUpdateWhere setType(StreetType type) {
    this.whereUpdater.setField("memory_never_be_superfluous.street_type", "type", type);
    return this;
  }

  @Override
  public StreetUpdateWhere setName(String name) {
    this.whereUpdater.setField("memory_never_be_superfluous.street_name", "name", name);
    return this;
  }



  @Override
  public StreetUpdateWhere whereIdIsEqualTo(long id) {
    this.whereUpdater.where("id", id);
    return this;
  }

  @Override
  public StreetUpdateWhere whereNameIsEqualTo(String name) {
    if (name == null) {
      throw new CannotBeNull("Field Street.name cannot be null");
    }
    this.whereUpdater.where("name", name);
    return this;
  }

  @Override
  public StreetUpdateWhere whereTypeIsEqualTo(StreetType type) {
    if (type == null) {
      throw new CannotBeNull("Field Street.type cannot be null");
    }
    this.whereUpdater.where("type", type);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
