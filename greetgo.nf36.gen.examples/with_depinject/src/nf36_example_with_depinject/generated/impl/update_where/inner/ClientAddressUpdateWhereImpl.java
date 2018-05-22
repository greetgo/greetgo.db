package nf36_example_with_depinject.generated.impl.update_where.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.inner.ClientAddressUpdateWhere;
import nf36_example_with_depinject.structure.AddressType;

public class ClientAddressUpdateWhereImpl implements ClientAddressUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public ClientAddressUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("client_address");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("client_id");
  }

  @Override
  public ClientAddressUpdateWhere setType(AddressType type) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_address_type", "type", type);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere setStreetId(long streetId) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_address_street_id", "street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere setHouse(String house) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_address_house", "house", house);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere setFlat(String flat) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_address_flat", "flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere setBirthDate(Date birthDate) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_address_birth_date", "birth_date", birthDate);
    return this;
  }



  @Override
  public ClientAddressUpdateWhere whereBirthDateIsEqualTo(Date birthDate) {
    if (birthDate == null) {
      throw new CannotBeNull("Field ClientAddress.birthDate cannot be null");
    }
    this.whereUpdater.where("birth_date", birthDate);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere whereClientIdIsEqualTo(long clientId) {
    this.whereUpdater.where("client_id", clientId);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere whereFlatIsEqualTo(String flat) {
    this.whereUpdater.where("flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere whereHouseIsEqualTo(String house) {
    this.whereUpdater.where("house", house);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere whereStreetIdIsEqualTo(long streetId) {
    this.whereUpdater.where("street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpdateWhere whereTypeIsEqualTo(AddressType type) {
    this.whereUpdater.where("type", type);
    return this;
  }

  @Override
  public void commit() {
    this.whereUpdater.commit();
  }
}
