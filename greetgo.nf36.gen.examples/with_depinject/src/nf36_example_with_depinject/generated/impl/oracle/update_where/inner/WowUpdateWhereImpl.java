package nf36_example_with_depinject.generated.impl.oracle.update_where.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.inner.WowUpdateWhere;

public class WowUpdateWhereImpl implements WowUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public WowUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("wow");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("wow_id", "wow_id2");
  }

  @Override
  public WowUpdateWhere setHello(String hello) {
    this.whereUpdater.setField("m_wow_hello", "hello", hello);
    return this;
  }



  @Override
  public WowUpdateWhere whereHelloIsEqualTo(String hello) {
    this.whereUpdater.where("hello", hello);
    return this;
  }

  @Override
  public WowUpdateWhere whereWowIdIsEqualTo(String wowId) {
    if (wowId == null) {
      throw new CannotBeNull("Field Wow.wowId cannot be null");
    }
    this.whereUpdater.where("wow_id", wowId);
    return this;
  }

  @Override
  public WowUpdateWhere whereWowId2IsEqualTo(String wowId2) {
    if (wowId2 == null) {
      throw new CannotBeNull("Field Wow.wowId2 cannot be null");
    }
    this.whereUpdater.where("wow_id2", wowId2);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
