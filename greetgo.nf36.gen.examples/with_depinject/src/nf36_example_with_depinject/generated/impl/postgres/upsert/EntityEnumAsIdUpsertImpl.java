package nf36_example_with_depinject.generated.impl.postgres.upsert;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.EntityEnumAsIdUpsert;
import nf36_example_with_depinject.structure.SomeEnum;

public class EntityEnumAsIdUpsertImpl implements EntityEnumAsIdUpsert {
  private final Nf36Upserter upserter;

  public EntityEnumAsIdUpsertImpl(Nf36Upserter upserter, SomeEnum id) {
    this.upserter = upserter;
    upserter.setNf3TableName("entity_enum_as_id");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id", id);
  }

  @Override
  public EntityEnumAsIdUpsert more(SomeEnum id) {
    return new EntityEnumAsIdUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public EntityEnumAsIdUpsert value(String value) {
    upserter.putField("memory_never_be_superfluous.entity_enum_as_id_value", "value", value);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
