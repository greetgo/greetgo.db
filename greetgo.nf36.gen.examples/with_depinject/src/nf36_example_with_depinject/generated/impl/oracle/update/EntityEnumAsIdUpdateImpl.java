package nf36_example_with_depinject.generated.impl.oracle.update;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import nf36_example_with_depinject.generated.faces.update.EntityEnumAsIdUpdate;
import nf36_example_with_depinject.structure.SomeEnum;

public class EntityEnumAsIdUpdateImpl implements EntityEnumAsIdUpdate {
  private final Nf36Updater updater;

  public EntityEnumAsIdUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("entity_enum_as_id");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public EntityEnumAsIdUpdate setValue(String value) {
    this.updater.setField("m_entity_enum_as_id_value", "value", value);
    return this;
  }



  @Override
  public EntityEnumAsIdUpdate whereIdIsEqualTo(SomeEnum id) {
    this.updater.where("id", id);
    return this;
  }

  @Override
  public EntityEnumAsIdUpdate whereValueIsEqualTo(String value) {
    this.updater.where("value", value);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
