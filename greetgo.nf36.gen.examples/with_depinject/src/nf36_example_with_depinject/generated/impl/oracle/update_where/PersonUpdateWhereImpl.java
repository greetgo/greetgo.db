package nf36_example_with_depinject.generated.impl.oracle.update_where;

import java.lang.String;
import java.math.BigDecimal;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.PersonUpdateWhere;

public class PersonUpdateWhereImpl implements PersonUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public PersonUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("person");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id");
  }

  @Override
  public PersonUpdateWhere setFio(String fio) {
    this.whereUpdater.setField("m_person_fio", "fio", fio);
    return this;
  }

  @Override
  public PersonUpdateWhere setBlocked(boolean blocked) {
    this.whereUpdater.setField("m_person_blocked", "blocked", blocked);
    return this;
  }

  @Override
  public PersonUpdateWhere setAmount(BigDecimal amount) {
    this.whereUpdater.setField("m_person_amount", "amount", amount);
    return this;
  }

  @Override
  public PersonUpdateWhere setAmountRU(BigDecimal amountRU) {
    this.whereUpdater.setField("m_person_amount_ru", "amount_ru", amountRU);
    return this;
  }



  @Override
  public PersonUpdateWhere whereAmountIsEqualTo(BigDecimal amount) {
    this.whereUpdater.where("amount", amount);
    return this;
  }

  @Override
  public PersonUpdateWhere whereAmountRUIsEqualTo(BigDecimal amountRU) {
    this.whereUpdater.where("amount_ru", amountRU);
    return this;
  }

  @Override
  public PersonUpdateWhere whereBlockedIsEqualTo(boolean blocked) {
    this.whereUpdater.where("blocked", blocked);
    return this;
  }

  @Override
  public PersonUpdateWhere whereFioIsEqualTo(String fio) {
    this.whereUpdater.where("fio", fio);
    return this;
  }

  @Override
  public PersonUpdateWhere whereIdIsEqualTo(String id) {
    if (id == null) {
      throw new CannotBeNull("Field Person.id cannot be null");
    }
    this.whereUpdater.where("id", id);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
