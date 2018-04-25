package kz.greetgo.db.nf36.gen.example.generated.impl.upsert;

import java.lang.String;
import java.math.BigDecimal;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.PersonUpsert;

public class PersonUpsertImpl implements PersonUpsert {
  private final Nf36Upserter upserter;

  public PersonUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.setNf3TableName("person");
    upserter.setTimeFieldName("ts");
    upserter.putId("id", id);
  }

  @Override
  public PersonUpsert fio(String fio) {
    upserter.putField("m_person_fio", "fio", fio);
    return this;
  }

  @Override
  public PersonUpsert blocked(boolean blocked) {
    upserter.putField("m_person_blocked", "blocked", blocked);
    return this;
  }

  @Override
  public PersonUpsert amount(BigDecimal amount) {
    upserter.putField("m_person_amount", "amount", amount);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNow("mod_at");
    upserter.commit();
  }
}
