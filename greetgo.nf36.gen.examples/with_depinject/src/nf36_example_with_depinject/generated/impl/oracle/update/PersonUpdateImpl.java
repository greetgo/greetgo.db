package nf36_example_with_depinject.generated.impl.oracle.update;

import java.lang.String;
import java.math.BigDecimal;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.PersonUpdate;

public class PersonUpdateImpl implements PersonUpdate {
  private final Nf36Updater updater;

  public PersonUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("person");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public PersonUpdate setFio(String fio) {
    this.updater.setField("m_person_fio", "fio", fio);
    return this;
  }

  @Override
  public PersonUpdate setBlocked(boolean blocked) {
    this.updater.setField("m_person_blocked", "blocked", blocked);
    return this;
  }

  @Override
  public PersonUpdate setAmount(BigDecimal amount) {
    this.updater.setField("m_person_amount", "amount", amount);
    return this;
  }

  @Override
  public PersonUpdate setAmountRU(BigDecimal amountRU) {
    this.updater.setField("m_person_amount_ru", "amount_ru", amountRU);
    return this;
  }



  @Override
  public PersonUpdate whereAmountIsEqualTo(BigDecimal amount) {
    this.updater.where("amount", amount);
    return this;
  }

  @Override
  public PersonUpdate whereAmountRUIsEqualTo(BigDecimal amountRU) {
    this.updater.where("amount_ru", amountRU);
    return this;
  }

  @Override
  public PersonUpdate whereBlockedIsEqualTo(boolean blocked) {
    this.updater.where("blocked", blocked);
    return this;
  }

  @Override
  public PersonUpdate whereFioIsEqualTo(String fio) {
    this.updater.where("fio", fio);
    return this;
  }

  @Override
  public PersonUpdate whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Person.id cannot be null");
    }
    this.updater.where("id", id);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
