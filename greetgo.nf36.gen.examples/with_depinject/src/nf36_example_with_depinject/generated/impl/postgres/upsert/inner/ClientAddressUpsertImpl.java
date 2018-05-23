package nf36_example_with_depinject.generated.impl.postgres.upsert.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.upsert.inner.ClientAddressUpsert;
import nf36_example_with_depinject.structure.AddressType;

public class ClientAddressUpsertImpl implements ClientAddressUpsert {
  private final Nf36Upserter upserter;

  public ClientAddressUpsertImpl(Nf36Upserter upserter, long clientId) {
    this.upserter = upserter;
    upserter.setNf3TableName("client_address");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("client_id", clientId);
  }

  public ClientAddressUpsert more(long clientId) {
    return new ClientAddressUpsertImpl(this.upserter.more(), clientId);
  }

  @Override
  public ClientAddressUpsert type(AddressType type) {
    upserter.putField("memory_never_be_superfluous.client_address_type", "type", type);
    return this;
  }

  @Override
  public ClientAddressUpsert streetId(long streetId) {
    upserter.putField("memory_never_be_superfluous.client_address_street_id", "street_id", streetId);
    return this;
  }

  @Override
  public ClientAddressUpsert house(String house) {
    upserter.putField("memory_never_be_superfluous.client_address_house", "house", house);
    return this;
  }

  @Override
  public ClientAddressUpsert flat(String flat) {
    upserter.putField("memory_never_be_superfluous.client_address_flat", "flat", flat);
    return this;
  }

  @Override
  public ClientAddressUpsert birthDate(Date birthDate) {
    if (birthDate == null) {
      throw new CannotBeNull("Field ClientAddress.birthDate cannot be null");
    }
    upserter.putField("memory_never_be_superfluous.client_address_birth_date", "birth_date", birthDate);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
