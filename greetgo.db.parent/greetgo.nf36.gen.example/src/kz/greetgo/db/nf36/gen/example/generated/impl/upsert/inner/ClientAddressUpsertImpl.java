package kz.greetgo.db.nf36.gen.example.generated.impl.upsert.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.core.Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ClientAddressUpsert;
import kz.greetgo.db.nf36.gen.example.structure.AddressType;

public class ClientAddressUpsertImpl implements ClientAddressUpsert {
  private final Upserter upserter;

  public ClientAddressUpsertImpl(Upserter upserter, long clientId) {
    this.upserter = upserter;
    upserter.putId("client_id", clientId);
  }

  @Override
  public ClientAddressUpsert type(AddressType type) {
    upserter.putField("type", type);
    return this;
  }

  @Override
  public ClientAddressUpsert streetId(long streetId) {
    upserter.putField("street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpsert house(String house) {
    upserter.putField("house", house);
    return this;
  }

  @Override
  public ClientAddressUpsert flat(String flat) {
    upserter.putField("flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpsert birthDate(Date birthDate) {
    upserter.putField("birth_date", birthDate);
    return this;
  }

  @Override
  public void go() {
    upserter.setTableName("client_address");
    upserter.setNf3Prefix("");
    upserter.setNf6Prefix("m_");
    upserter.go();
  }
}
