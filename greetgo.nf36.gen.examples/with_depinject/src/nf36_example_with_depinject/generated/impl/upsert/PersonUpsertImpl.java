package nf36_example_with_depinject.generated.impl.upsert;

import java.lang.String;
import java.math.BigDecimal;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;

public class PersonUpsertImpl implements PersonUpsert {
  private final Nf36Upserter upserter;

  public PersonUpsertImpl(Nf36Upserter upserter, String id) {
    this.upserter = upserter;
    upserter.setNf3TableName("person");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id", id);
  }

  public PersonUpsert more(String id) {
    return new PersonUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public PersonUpsert fio(String fio) {
    upserter.putField("memory_never_be_superfluous.person_fio", "fio", fio);
    return this;
  }

  @Override
  public PersonUpsert blocked(boolean blocked) {
    upserter.putField("memory_never_be_superfluous.person_blocked", "blocked", blocked);
    return this;
  }

  @Override
  public PersonUpsert amount(BigDecimal amount) {
    upserter.putField("memory_never_be_superfluous.person_amount", "amount", amount);
    return this;
  }

  @Override
  public PersonUpsert amountRU(BigDecimal amountRU) {
    upserter.putField("memory_never_be_superfluous.person_amount_ru", "amount_ru", amountRU);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
