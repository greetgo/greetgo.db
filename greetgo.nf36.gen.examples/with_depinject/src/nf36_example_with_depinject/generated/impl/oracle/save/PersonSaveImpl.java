package nf36_example_with_depinject.generated.impl.oracle.save;

import java.lang.Boolean;
import java.lang.String;
import java.math.BigDecimal;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.PersonSave;

public class PersonSaveImpl implements PersonSave {
  private final Nf36Saver saver13;

  public PersonSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("person");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("id");
    saver.addFieldName("m_person_fio", "fio");
    saver.addFieldName("m_person_blocked", "blocked");
    saver.addFieldName("m_person_amount", "amount");
    saver.addFieldName("m_person_amount_ru", "amount_ru");
  }

  private final fio fio = new fio() {
    @Override
    public PersonSave set(String value) {
      saver13.presetValue("fio", value);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("fio", predicate);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave alias(String alias) {
      saver13.addAlias("fio", alias);
      return PersonSaveImpl.this;
    }

  };

  @Override
  public fio fio() {
    return fio;
  }

  private final blocked blocked = new blocked() {
    @Override
    public PersonSave set(boolean value) {
      saver13.presetValue("blocked", value);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave skipIf(Predicate<Boolean>  predicate) {
      saver13.addSkipIf("blocked", predicate);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave alias(String alias) {
      saver13.addAlias("blocked", alias);
      return PersonSaveImpl.this;
    }

  };

  @Override
  public blocked blocked() {
    return blocked;
  }

  private final amount amount = new amount() {
    @Override
    public PersonSave set(BigDecimal value) {
      saver13.presetValue("amount", value);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave skipIf(Predicate<BigDecimal>  predicate) {
      saver13.addSkipIf("amount", predicate);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave alias(String alias) {
      saver13.addAlias("amount", alias);
      return PersonSaveImpl.this;
    }

  };

  @Override
  public amount amount() {
    return amount;
  }

  private final amountRU amountRU = new amountRU() {
    @Override
    public PersonSave set(BigDecimal value) {
      saver13.presetValue("amountRU", value);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave skipIf(Predicate<BigDecimal>  predicate) {
      saver13.addSkipIf("amountRU", predicate);
      return PersonSaveImpl.this;
    }

    @Override
    public PersonSave alias(String alias) {
      saver13.addAlias("amountRU", alias);
      return PersonSaveImpl.this;
    }

  };

  @Override
  public amountRU amountRU() {
    return amountRU;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
