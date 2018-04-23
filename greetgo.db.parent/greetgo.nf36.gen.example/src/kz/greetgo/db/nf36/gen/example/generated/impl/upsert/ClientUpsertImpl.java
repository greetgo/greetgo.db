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
    upserter.putId("id", id);
  }

  @Override
  public ClientUpsert surname(String surname) {
    if (surname == null) {
      throw new CannotBeNull("Field Client.surname cannot be null");
    }
    upserter.putField("LEFT", "surname", surname);
    return this;
  }

  @Override
  public ClientUpsert name(String name) {
    upserter.putField("LEFT", "name", name);
    return this;
  }

  @Override
  public ClientUpsert patronymic(String patronymic) {
    upserter.putField("LEFT", "patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpsert charmId(String charmId) {
    if (charmId == null) {
      throw new CannotBeNull("Field Client.charmId cannot be null");
    }
    upserter.putField("LEFT", "charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpsert longDescription(String longDescription) {
    upserter.putField("LEFT", "long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpsert myChairId1(Long myChairId1) {
    upserter.putField("LEFT", "my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpsert myChairId2(String myChairId2) {
    if (myChairId2 == null) {
      throw new CannotBeNull("Field Client.myChairId2 cannot be null");
    }
    upserter.putField("LEFT", "my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpsert hisChairLongId(Long hisChairLongId) {
    upserter.putField("LEFT", "his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpsert hisChairStrId(String hisChairStrId) {
    if (hisChairStrId == null) {
      throw new CannotBeNull("Field Client.hisChairStrId cannot be null");
    }
    upserter.putField("LEFT", "his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public void commit() {
    upserter.commit();
  }
}
