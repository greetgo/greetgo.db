package kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ChairUpsert;

public class ChairUpsertImpl implements ChairUpsert {
  private final Nf36Upserter upserter;

  public ChairUpsertImpl(Nf36Upserter upserter, long id1, String id2) {
    this.upserter = upserter;
    upserter.setNf3TableName("chair");
    upserter.setTimeFieldName("ts");
    upserter.putId("id1", id1);
    upserter.putId("id2", id2);
  }

  @Override
  public ChairUpsert name(String name) {
    upserter.putField("m_chair_name", "name", name);
    return this;
  }

  @Override
  public ChairUpsert description(String description) {
    upserter.putField("m_chair_description", "description", description);
    return this;
  }

  @Override
  public void commit() {
    upserter.commit();
  }
}
