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
    upserter.putId("id", id);
  }

  @Override
  public StreetUpsert type(StreetType type) {
    if (type == null) {
      throw new CannotBeNull("Field Street.type cannot be null");
    }
    upserter.putField("LEFT", "type", type);
    return this;
  }

  @Override
  public StreetUpsert name(String name) {
    if (name == null) {
      throw new CannotBeNull("Field Street.name cannot be null");
    }
    upserter.putField("LEFT", "name", name);
    return this;
  }

  @Override
  public void commit() {
    upserter.commit();
  }
}
