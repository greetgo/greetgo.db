package nf36_example_with_depinject.generated.faces.upsert.inner;

import java.lang.String;
import java.util.Date;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.structure.AddressType;

public interface ClientAddressUpsert {
  ClientAddressUpsert type(AddressType type);

  ClientAddressUpsert streetId(long streetId);

  ClientAddressUpsert house(String house);

  ClientAddressUpsert flat(String flat);

  ClientAddressUpsert birthDate(Date birthDate);

  ClientAddressUpsert more(long clientId);

  void commit();
}
