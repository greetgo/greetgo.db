package kz.greetgo.db.nf36.gen.example.generated.impl.upsert;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.StreetUpsert;
import kz.greetgo.db.nf36.gen.example.structure.StreetType;

public class StreetUpsertImpl implements StreetUpsert {
  private final Nf36Upserter upserter;

  public StreetUpsertImpl(Nf36Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.putId("id", id);
  }

  @Override
  public StreetUpsert type(StreetType type) {
    upserter.putField("type", type);
    return this;
  }

  @Override
  public StreetUpsert name(String name) {
    upserter.putField("name", name);
    return this;
  }

  @Override
  public void go() {
    upserter.setTableName("street");
    upserter.setNf3Prefix("");
    upserter.setNf6Prefix("m_");
    upserter.go();
  }
}
