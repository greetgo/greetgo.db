package nf36_example_with_depinject.generated.impl.oracle.update;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.ManyIdsUpdate;

public class ManyIdsUpdateImpl implements ManyIdsUpdate {
  private final Nf36Updater updater;

  public ManyIdsUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("many_ids");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("int_id", "boxed_int_id", "long_id", "boxed_long_id", "str_id");
  }

  @Override
  public ManyIdsUpdate setAField(int aField) {
    this.updater.setField("m_many_ids_a_field", "a_field", aField);
    return this;
  }



  @Override
  public ManyIdsUpdate whereAFieldIsEqualTo(int aField) {
    this.updater.where("a_field", aField);
    return this;
  }

  @Override
  public ManyIdsUpdate whereBoxedIntIdIsEqualTo(Integer boxedIntId) {
    this.updater.where("boxed_int_id", boxedIntId);
    return this;
  }

  @Override
  public ManyIdsUpdate whereBoxedLongIdIsEqualTo(Long boxedLongId) {
    this.updater.where("boxed_long_id", boxedLongId);
    return this;
  }

  @Override
  public ManyIdsUpdate whereIntIdIsEqualTo(int intId) {
    this.updater.where("int_id", intId);
    return this;
  }

  @Override
  public ManyIdsUpdate whereLongIdIsEqualTo(long longId) {
    this.updater.where("long_id", longId);
    return this;
  }

  @Override
  public ManyIdsUpdate whereStrIdIsEqualTo(String strId) {
    if (strId == null) {
      throw new CannotBeNull("Field ManyIds.strId cannot be null");
    }
    this.updater.where("str_id", strId);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
