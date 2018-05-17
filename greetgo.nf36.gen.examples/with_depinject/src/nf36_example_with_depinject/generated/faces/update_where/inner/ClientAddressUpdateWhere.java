package nf36_example_with_depinject.generated.faces.update_where.inner;

import java.lang.String;
import java.util.Date;
import nf36_example_with_depinject.structure.AddressType;

public interface ClientAddressUpdateWhere {
  ClientAddressUpdateWhere setBirthDate(Date birthDate);

  ClientAddressUpdateWhere setFlat(String flat);

  ClientAddressUpdateWhere setHouse(String house);

  ClientAddressUpdateWhere setStreetId(long streetId);

  ClientAddressUpdateWhere setType(AddressType type);


  ClientAddressUpdateWhere whereBirthDateIsEqualTo(Date birthDate);

  ClientAddressUpdateWhere whereClientIdIsEqualTo(long clientId);

  ClientAddressUpdateWhere whereFlatIsEqualTo(String flat);

  ClientAddressUpdateWhere whereHouseIsEqualTo(String house);

  ClientAddressUpdateWhere whereStreetIdIsEqualTo(long streetId);

  ClientAddressUpdateWhere whereTypeIsEqualTo(AddressType type);

  void commit();
}
