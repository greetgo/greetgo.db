package nf36_example_with_depinject.generated.impl.postgres.upsert;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.EntityWithManyIdsUpsert;

public class EntityWithManyIdsUpsertImpl implements EntityWithManyIdsUpsert {
  private final Nf36Upserter upserter;

  public EntityWithManyIdsUpsertImpl(Nf36Upserter upserter, int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    this.upserter = upserter;
    upserter.setNf3TableName("entity_with_many_ids");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("int_id", intId);
    upserter.putId("boxed_int_id", boxedIntId);
    upserter.putId("long_id", longId);
    upserter.putId("boxed_long_id", boxedLongId);
    upserter.putId("str_id", strId);
  }

  @Override
  public EntityWithManyIdsUpsert more(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId) {
    return new EntityWithManyIdsUpsertImpl(this.upserter.more(), intId, boxedIntId, longId, boxedLongId, strId);
  }

  @Override
  public EntityWithManyIdsUpsert aField(int aField) {
    upserter.putField("memory_never_be_superfluous.entity_with_many_ids_a_field", "a_field", aField);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
