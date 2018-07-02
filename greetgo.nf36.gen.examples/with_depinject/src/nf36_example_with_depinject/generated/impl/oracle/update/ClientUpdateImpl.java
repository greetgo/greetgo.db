package nf36_example_with_depinject.generated.impl.oracle.update;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update.ClientUpdate;

public class ClientUpdateImpl implements ClientUpdate {
  private final Nf36Updater updater;

  public ClientUpdateImpl(Nf36Updater updater) {
    this.updater = updater;
    updater.setNf3TableName("client");
    updater.setAuthorFieldNames("modified_by", "inserted_by");
    updater.updateFieldToNow("mod_at");
    updater.setIdFieldNames("id");
  }

  @Override
  public ClientUpdate setSurname(String surname) {
    this.updater.setField("m_client_surname", "surname", surname);
    return this;
  }

  @Override
  public ClientUpdate setName(String name) {
    this.updater.setField("m_client_name", "name", name);
    return this;
  }

  @Override
  public ClientUpdate setPatronymic(String patronymic) {
    this.updater.setField("m_client_patronymic", "patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpdate setCharmId(String charmId) {
    this.updater.setField("m_client_charm_id", "charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpdate setLongDescription(String longDescription) {
    this.updater.setField("m_client_long_description", "long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpdate setMyChairId1(Long myChairId1) {
    this.updater.setField("m_client_my_chair_id1", "my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpdate setMyChairId2(String myChairId2) {
    this.updater.setField("m_client_my_chair_id1", "my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpdate setHisChairLongId(Long hisChairLongId) {
    this.updater.setField("m_client_his_chair_long_id", "his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpdate setHisChairStrId(String hisChairStrId) {
    this.updater.setField("m_client_his_chair_long_id", "his_chair_str_id", hisChairStrId);
    return this;
  }



  @Override
  public ClientUpdate whereCharmIdIsEqualTo(String charmId) {
    this.updater.where("charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpdate whereHisChairLongIdIsEqualTo(Long hisChairLongId) {
    this.updater.where("his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpdate whereHisChairStrIdIsEqualTo(String hisChairStrId) {
    this.updater.where("his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public ClientUpdate whereIdIsEqualTo(long id) {
    this.updater.where("id", id);
    return this;
  }

  @Override
  public ClientUpdate whereLongDescriptionIsEqualTo(String longDescription) {
    this.updater.where("long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpdate whereMyChairId1IsEqualTo(Long myChairId1) {
    this.updater.where("my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpdate whereMyChairId2IsEqualTo(String myChairId2) {
    this.updater.where("my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpdate whereNameIsEqualTo(String name) {
    this.updater.where("name", name);
    return this;
  }

  @Override
  public ClientUpdate wherePatronymicIsEqualTo(String patronymic) {
    this.updater.where("patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpdate whereSurnameIsEqualTo(String surname) {
    if (surname == null) {
      throw new CannotBeNull("Field Client.surname cannot be null");
    }
    this.updater.where("surname", surname);
    return this;
  }

  @Override
  public void commitAll() {
    this.updater.commit();
  }
}
