package kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ClientAddressUpsert;
import kz.greetgo.db.nf36.gen.example.structure.AddressType;

public interface ClientAddressUpsert {
  ClientAddressUpsert type(AddressType type);

  ClientAddressUpsert streetId(long streetId);

  ClientAddressUpsert house(String house);

  ClientAddressUpsert flat(String flat);

  ClientAddressUpsert birthDate(Date birthDate);

  ClientAddressUpsert more(long clientId);

  void commit();
}
