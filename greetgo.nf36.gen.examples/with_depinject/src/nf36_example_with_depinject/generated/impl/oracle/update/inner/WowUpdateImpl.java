package nf36_example_with_depinject.generated.impl.oracle.update.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.inner.WowUpdate;

public class WowUpdateImpl implements WowUpdate {
  private final Nf36Updater updater;

  public WowUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("wow");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("wow_id", "wow_id2");
  }

  @Override
  public WowUpdate setHello(String hello) {
    this.updater.setField("m_wow_hello", "hello", hello);
    return this;
  }



  @Override
  public WowUpdate whereHelloIsEqualTo(String hello) {
    this.updater.where("hello", hello);
    return this;
  }

  @Override
  public WowUpdate whereWowIdIsEqualTo(String wowId) {
    if (wowId == null) {
      throw new CannotBeNull("Field Wow.wowId cannot be null");
    }
    this.updater.where("wow_id", wowId);
    return this;
  }

  @Override
  public WowUpdate whereWowId2IsEqualTo(String wowId2) {
    if (wowId2 == null) {
      throw new CannotBeNull("Field Wow.wowId2 cannot be null");
    }
    this.updater.where("wow_id2", wowId2);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
