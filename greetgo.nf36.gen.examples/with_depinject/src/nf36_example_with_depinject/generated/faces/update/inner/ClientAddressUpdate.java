package nf36_example_with_depinject.generated.faces.update.inner;

import java.lang.String;
import java.util.Date;
import nf36_example_with_depinject.structure.AddressType;

public interface ClientAddressUpdate {
  ClientAddressUpdate setBirthDate(Date birthDate);

  ClientAddressUpdate setFlat(String flat);

  ClientAddressUpdate setHouse(String house);

  ClientAddressUpdate setStreetId(long streetId);

  ClientAddressUpdate setType(AddressType type);


  ClientAddressUpdate whereBirthDateIsEqualTo(Date birthDate);

  ClientAddressUpdate whereClientIdIsEqualTo(long clientId);

  ClientAddressUpdate whereFlatIsEqualTo(String flat);

  ClientAddressUpdate whereHouseIsEqualTo(String house);

  ClientAddressUpdate whereStreetIdIsEqualTo(long streetId);

  ClientAddressUpdate whereTypeIsEqualTo(AddressType type);

  void commit();
}
