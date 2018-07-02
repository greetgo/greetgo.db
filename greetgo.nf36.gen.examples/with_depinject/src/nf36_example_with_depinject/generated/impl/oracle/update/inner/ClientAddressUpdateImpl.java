package nf36_example_with_depinject.generated.impl.oracle.update.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.inner.ClientAddressUpdate;
import nf36_example_with_depinject.structure.AddressType;

public class ClientAddressUpdateImpl implements ClientAddressUpdate {
  private final Nf36Updater updater;

  public ClientAddressUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("client_address");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("client_id");
  }

  @Override
  public ClientAddressUpdate setType(AddressType type) {
    this.updater.setField("m_client_address_type", "type", type);
    return this;
  }

  @Override
  public ClientAddressUpdate setStreetId(long streetId) {
    this.updater.setField("m_client_address_street_id", "street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpdate setHouse(String house) {
    this.updater.setField("m_client_address_house", "house", house);
    return this;
  }

  @Override
  public ClientAddressUpdate setFlat(String flat) {
    this.updater.setField("m_client_address_flat", "flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpdate setBirthDate(Date birthDate) {
    this.updater.setField("m_client_address_birth_date", "birth_date", birthDate);
    return this;
  }



  @Override
  public ClientAddressUpdate whereBirthDateIsEqualTo(Date birthDate) {
    if (birthDate == null) {
      throw new CannotBeNull("Field ClientAddress.birthDate cannot be null");
    }
    this.updater.where("birth_date", birthDate);
    return this;
  }

  @Override
  public ClientAddressUpdate whereClientIdIsEqualTo(long clientId) {
    this.updater.where("client_id", clientId);
    return this;
  }

  @Override
  public ClientAddressUpdate whereFlatIsEqualTo(String flat) {
    this.updater.where("flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpdate whereHouseIsEqualTo(String house) {
    this.updater.where("house", house);
    return this;
  }

  @Override
  public ClientAddressUpdate whereStreetIdIsEqualTo(long streetId) {
    this.updater.where("street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpdate whereTypeIsEqualTo(AddressType type) {
    this.updater.where("type", type);
    return this;
  }

  @Override
  public void commit() {
    this.updater.commit();
  }
}
