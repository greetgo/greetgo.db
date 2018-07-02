package nf36_example_with_depinject.generated.impl.oracle.update;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.StreetUpdate;
import nf36_example_with_depinject.structure.StreetType;

public class StreetUpdateImpl implements StreetUpdate {
  private final Nf36Updater updater;

  public StreetUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("street");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public StreetUpdate setType(StreetType type) {
    this.updater.setField("m_street_type", "type", type);
    return this;
  }

  @Override
  public StreetUpdate setName(String name) {
    this.updater.setField("m_street_name", "name", name);
    return this;
  }



  @Override
  public StreetUpdate whereIdIsEqualTo(long id) {
    this.updater.where("id", id);
    return this;
  }

  @Override
  public StreetUpdate whereNameIsEqualTo(String name) {
    if (name == null) {
      throw new CannotBeNull("Field Street.name cannot be null");
    }
    this.updater.where("name", name);
    return this;
  }

  @Override
  public StreetUpdate whereTypeIsEqualTo(StreetType type) {
    if (type == null) {
      throw new CannotBeNull("Field Street.type cannot be null");
    }
    this.updater.where("type", type);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
