package nf36_example_with_depinject.generated.impl.oracle.upsert;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.ManyIdsUpsert;

public class ManyIdsUpsertImpl implements ManyIdsUpsert {
  private final Nf36Upserter upserter;

  public ManyIdsUpsertImpl(Nf36Upserter upserter, int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    this.upserter = upserter;
    upserter.setNf3TableName("many_ids");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("int_id", intId);
    upserter.putId("boxed_int_id", boxedIntId);
    upserter.putId("long_id", longId);
    upserter.putId("boxed_long_id", boxedLongId);
    upserter.putId("str_id", strId);
  }

  @Override
  public ManyIdsUpsert more(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    return new ManyIdsUpsertImpl(this.upserter.more(), intId, boxedIntId, longId, boxedLongId, strId);
  }

  @Override
  public ManyIdsUpsert aField(int aField) {
    upserter.putField("m_many_ids_a_field", "a_field", aField);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
