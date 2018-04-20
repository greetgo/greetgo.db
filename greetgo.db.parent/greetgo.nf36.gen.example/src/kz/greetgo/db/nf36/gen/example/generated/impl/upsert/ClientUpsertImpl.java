package kz.greetgo.db.nf36.gen.example.generated.impl.upsert;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Upserter;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.ClientUpsert;

public class ClientUpsertImpl implements ClientUpsert {
  private final Upserter upserter;

  public ClientUpsertImpl(Upserter upserter, long id) {
    this.upserter = upserter;
    upserter.putId("id", id);
  }

  @Override
  public ClientUpsert surname(String surname) {
    upserter.putField("surname", surname);
    return this;
  }

  @Override
  public ClientUpsert name(String name) {
    upserter.putField("name", name);
    return this;
  }

  @Override
  public ClientUpsert patronymic(String patronymic) {
    upserter.putField("patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpsert longDescription(String longDescription) {
    upserter.putField("long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpsert myChairId1(Long myChairId1) {
    upserter.putField("my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpsert myChairId2(String myChairId2) {
    upserter.putField("my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpsert hisChairLongId(Long hisChairLongId) {
    upserter.putField("his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpsert hisChairStrId(String hisChairStrId) {
    upserter.putField("his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public void go() {
    upserter.setTableName("client");
    upserter.setNf3Prefix("");
    upserter.setNf6Prefix("m_");
    upserter.go();
  }
}
