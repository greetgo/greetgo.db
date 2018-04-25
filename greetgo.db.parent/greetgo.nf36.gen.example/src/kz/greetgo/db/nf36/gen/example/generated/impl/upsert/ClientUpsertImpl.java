package kz.greetgo.db.nf36.gen.example.generated.impl.upsert;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;

public class ClientUpsertImpl implements ClientUpsert {
  private final Nf36Upserter upserter;

  public ClientUpsertImpl(Nf36Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.setNf3TableName("client");
    upserter.setTimeFieldName("ts");
    upserter.putId("id", id);
  }

  @Override
  public ClientUpsert surname(String surname) {
    if (surname == null) {
      throw new CannotBeNull("Field Client.surname cannot be null");
    }
    upserter.putField("m_client_surname", "surname", surname);
    return this;
  }

  @Override
  public ClientUpsert name(String name) {
    upserter.putField("m_client_name", "name", name);
    return this;
  }

  @Override
  public ClientUpsert patronymic(String patronymic) {
    upserter.putField("m_client_patronymic", "patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpsert charmId(String charmId) {
    if (charmId == null) {
      throw new CannotBeNull("Field Client.charmId cannot be null");
    }
    upserter.putField("m_client_charm_id", "charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpsert longDescription(String longDescription) {
    upserter.putField("m_client_long_description", "long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpsert myChairId1(Long myChairId1) {
    upserter.putField("m_client_my_chair_id1", "my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpsert myChairId2(String myChairId2) {
    if (myChairId2 == null) {
      throw new CannotBeNull("Field Client.myChairId2 cannot be null");
    }
    upserter.putField("m_client_my_chair_id1", "my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpsert hisChairLongId(Long hisChairLongId) {
    upserter.putField("m_client_his_chair_long_id", "his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpsert hisChairStrId(String hisChairStrId) {
    if (hisChairStrId == null) {
      throw new CannotBeNull("Field Client.hisChairStrId cannot be null");
    }
    upserter.putField("m_client_his_chair_long_id", "his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public void commit() {
    upserter.putUpdateToNow("mod_at");
    upserter.commit();
  }
}
