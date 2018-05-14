package nf36_example_with_depinject.generated.impl.upsert;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;

import java.math.BigDecimal;

public class PersonUpsertImpl implements PersonUpsert {
  private final Nf36Upserter upserter;

  public PersonUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.setNf3TableName("person");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.setTimeFieldName("ts");
    upserter.putId("id", id);
  }

  public PersonUpsert more(String id) {
    return new PersonUpsertImpl(this.upserter.more(), id);
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
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
