package nf36_example_with_depinject.generated.impl.oracle.save.inner;

import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.function.Predicate;
import kz.greetgo.db.nf36.core.Nf36Saver;
import nf36_example_with_depinject.generated.faces.save.inner.ClientAddressSave;
import nf36_example_with_depinject.structure.AddressType;

public class ClientAddressSaveImpl implements ClientAddressSave {
  private final Nf36Saver saver13;

  public ClientAddressSaveImpl(Nf36Saver saver) {
    this.saver13 = saver;
    saver.setNf3TableName("client_address");
    saver.setTimeFieldName("ts");
    saver.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    saver.addIdName("client_id");
    saver.addFieldName("m_client_address_type", "type");
    saver.addFieldName("m_client_address_street_id", "street_id");
    saver.addFieldName("m_client_address_house", "house");
    saver.addFieldName("m_client_address_flat", "flat");
    saver.addFieldName("m_client_address_birth_date", "birth_date");
  }

  private final type type = new type() {
    @Override
    public ClientAddressSave set(AddressType value) {
      saver13.presetValue("type", value);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave skipIf(Predicate<AddressType>  predicate) {
      saver13.addSkipIf("type", predicate);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave alias(String alias) {
      saver13.addAlias("type", alias);
      return ClientAddressSaveImpl.this;
    }

  };

  @Override
  public type type() {
    return type;
  }

  private final streetId streetId = new streetId() {
    @Override
    public ClientAddressSave set(long value) {
      saver13.presetValue("streetId", value);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave skipIf(Predicate<Long>  predicate) {
      saver13.addSkipIf("streetId", predicate);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave alias(String alias) {
      saver13.addAlias("streetId", alias);
      return ClientAddressSaveImpl.this;
    }

  };

  @Override
  public streetId streetId() {
    return streetId;
  }

  private final house house = new house() {
    @Override
    public ClientAddressSave set(String value) {
      saver13.presetValue("house", value);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("house", predicate);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave alias(String alias) {
      saver13.addAlias("house", alias);
      return ClientAddressSaveImpl.this;
    }

  };

  @Override
  public house house() {
    return house;
  }

  private final flat flat = new flat() {
    @Override
    public ClientAddressSave set(String value) {
      saver13.presetValue("flat", value);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave skipIf(Predicate<String>  predicate) {
      saver13.addSkipIf("flat", predicate);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave alias(String alias) {
      saver13.addAlias("flat", alias);
      return ClientAddressSaveImpl.this;
    }

  };

  @Override
  public flat flat() {
    return flat;
  }

  private final birthDate birthDate = new birthDate() {
    @Override
    public ClientAddressSave set(Date value) {
      saver13.presetValue("birthDate", value);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave skipIf(Predicate<Date>  predicate) {
      saver13.addSkipIf("birthDate", predicate);
      return ClientAddressSaveImpl.this;
    }

    @Override
    public ClientAddressSave alias(String alias) {
      saver13.addAlias("birthDate", alias);
      return ClientAddressSaveImpl.this;
    }

  };

  @Override
  public birthDate birthDate() {
    return birthDate;
  }

  @Override
  public void save(Object objectWithData) {
    saver13.putUpdateToNow("mod_at");
    saver13.save(objectWithData);
  }
}
