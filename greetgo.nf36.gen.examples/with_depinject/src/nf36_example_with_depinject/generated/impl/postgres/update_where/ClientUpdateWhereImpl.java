package nf36_example_with_depinject.generated.impl.postgres.update_where;

import java.lang.Long;
import java.lang.String;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import nf36_example_with_depinject.generated.faces.update_where.ClientUpdateWhere;

public class ClientUpdateWhereImpl implements ClientUpdateWhere {
  private final Nf36WhereUpdater whereUpdater;

  public ClientUpdateWhereImpl(Nf36WhereUpdater whereUpdater) {
    this.whereUpdater = whereUpdater;
    whereUpdater.setNf3TableName("client");
    whereUpdater.setAuthorFieldNames("modified_by", "inserted_by");
    whereUpdater.updateFieldToNow("mod_at");
    whereUpdater.setIdFieldNames("id");
  }

  @Override
  public ClientUpdateWhere setSurname(String surname) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_surname", "surname", surname);
    return this;
  }

  @Override
  public ClientUpdateWhere setName(String name) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_name", "name", name);
    return this;
  }

  @Override
  public ClientUpdateWhere setPatronymic(String patronymic) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_patronymic", "patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpdateWhere setCharmId(String charmId) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_charm_id", "charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpdateWhere setLongDescription(String longDescription) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_long_description", "long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpdateWhere setMyChairId1(Long myChairId1) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpdateWhere setMyChairId2(String myChairId2) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_my_chair_id1", "my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpdateWhere setHisChairLongId(Long hisChairLongId) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpdateWhere setHisChairStrId(String hisChairStrId) {
    this.whereUpdater.setField("memory_never_be_superfluous.client_his_chair_long_id", "his_chair_str_id", hisChairStrId);
    return this;
  }



  @Override
  public ClientUpdateWhere whereCharmIdIsEqualTo(String charmId) {
    this.whereUpdater.where("charm_id", charmId);
    return this;
  }

  @Override
  public ClientUpdateWhere whereHisChairLongIdIsEqualTo(Long hisChairLongId) {
    this.whereUpdater.where("his_chair_long_id", hisChairLongId);
    return this;
  }

  @Override
  public ClientUpdateWhere whereHisChairStrIdIsEqualTo(String hisChairStrId) {
    this.whereUpdater.where("his_chair_str_id", hisChairStrId);
    return this;
  }

  @Override
  public ClientUpdateWhere whereIdIsEqualTo(long id) {
    this.whereUpdater.where("id", id);
    return this;
  }

  @Override
  public ClientUpdateWhere whereLongDescriptionIsEqualTo(String longDescription) {
    this.whereUpdater.where("long_description", longDescription);
    return this;
  }

  @Override
  public ClientUpdateWhere whereMyChairId1IsEqualTo(Long myChairId1) {
    this.whereUpdater.where("my_chair_id1", myChairId1);
    return this;
  }

  @Override
  public ClientUpdateWhere whereMyChairId2IsEqualTo(String myChairId2) {
    this.whereUpdater.where("my_chair_id2", myChairId2);
    return this;
  }

  @Override
  public ClientUpdateWhere whereNameIsEqualTo(String name) {
    this.whereUpdater.where("name", name);
    return this;
  }

  @Override
  public ClientUpdateWhere wherePatronymicIsEqualTo(String patronymic) {
    this.whereUpdater.where("patronymic", patronymic);
    return this;
  }

  @Override
  public ClientUpdateWhere whereSurnameIsEqualTo(String surname) {
    if (surname == null) {
      throw new CannotBeNull("Field Client.surname cannot be null");
    }
    this.whereUpdater.where("surname", surname);
    return this;
  }

  @Override
  public void commitAll() {
    this.whereUpdater.commit();
  }
}
