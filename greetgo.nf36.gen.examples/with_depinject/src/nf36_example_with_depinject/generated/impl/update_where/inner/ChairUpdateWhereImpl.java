package nf36_example_with_depinject.generated.impl.update_where.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.inner.ChairUpdateWhere;

public class ChairUpdateWhereImpl implements ChairUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public ChairUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("chair");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id1", "id2");
  }

  @Override
  public ChairUpdateWhere setName(String name) {
    this.whereUpdater.setField("m_chair_name", "name", name);
    return this;
  }

  @Override
  public ChairUpdateWhere setDescription(String description) {
    this.whereUpdater.setField("m_chair_description", "description", description);
    return this;
  }



  @Override
  public ChairUpdateWhere whereDescriptionIsEqualTo(String description) {
    this.whereUpdater.where("description", description);
    return this;
  }

  @Override
  public ChairUpdateWhere whereId1IsEqualTo(long id1) {
    this.whereUpdater.where("id1", id1);
    return this;
  }

  @Override
  public ChairUpdateWhere whereId2IsEqualTo(String id2) {
    if (id2 == null) {
      throw new CannotBeNull("Field Chair.id2 cannot be null");
    }
    this.whereUpdater.where("id2", id2);
    return this;
  }

  @Override
  public ChairUpdateWhere whereNameIsEqualTo(String name) {
    this.whereUpdater.where("name", name);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
