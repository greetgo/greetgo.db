package nf36_example_with_depinject.generated.impl.postgres.upsert;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.upsert.ClientUpsert;

public class ClientUpsertImpl implements ClientUpsert {
  private final Nf36Upserter upserter;

  public ClientUpsertImpl(Nf36Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.setNf3TableName("client");
    upserter.setTimeFieldName("ts");
    upserter.setAuthorFieldNames("created_by", "modified_by", "inserted_by");
    upserter.putId("id", id);
  }

  public ClientUpsert moreAnother(long id) {
    return new ClientUpsertImpl(this.upserter.more(), id);
  }

  @Override
  public ClientUpsert surname(String surname) {
    if (surname == null) {
      throw new CannotBeNull("Field Client.surname cannot be null");
    }
    upserter.putField("memory_never_be_superfluous.client_surname", "surname", surname);
    return this;
  }

  @Override
  public ClientUpsert name(String name) {
    upserter.putField("memory_never_be_superfluous.client_name", "name", name);
    return this;
  }

  @Override
  public ClientUpsert patronymic(String patronymic) {
    upserter.putField("memory_never_be_superfluous.client_patronymic", "patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpsert charmId(String charmId) {
    upserter.putField("memory_never_be_superfluous.client_charm_id", "charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpsert longDescription(String longDescription) {
    upserter.putField("memory_never_be_superfluous.client_long_description", "long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpsert myChairId1(Long myChairId1) {
    upserter.putField("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpsert myChairId2(String myChairId2) {
    upserter.putField("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpsert hisChairLongId(Long hisChairLongId) {
    upserter.putField("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpsert hisChairStrId(String hisChairStrId) {
    upserter.putField("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public void commitAll() {
    upserter.putUpdateToNowWithParent("mod_at");
    upserter.commit();
  }
}
