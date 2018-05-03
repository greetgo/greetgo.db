package kz.greetgo.db.nf36.gen.example.generated.impl.upsert;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.StreetUpsert;
import kz.greetgo.db.nf36.gen.example.structure.StreetType;

public class StreetUpsertImpl implements StreetUpsert {
  private final Nf36Upserter upserter;

  public StreetUpsertImpl(Nf36Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.setNf3TableName("street");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.setTimeFieldName("ts");
    upserter.putId("id", id);
  }

  public StreetUpsert more(long id) {
    return null;
  }

  @Override
  public StreetUpsert type(StreetType type) {
    if (type == null) {
      throw new CannotBeNull("Field Street.type cannot be null");
    }
    upserter.putField("m_street_type", "type", type);
    return this;
  }

  @Override
  public StreetUpsert name(String name) {
    if (name == null) {
      throw new CannotBeNull("Field Street.name cannot be null");
    }
    upserter.putField("m_street_name", "name", name);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNow("mod_at");
    upserter.commit();
  }
}
