package nf36_example_with_depinject.generated.faces.update_where;

import java.lang.Long;
import java.lang.String;

public interface ClientUpdateWhere {
  ClientUpdateWhere setCharmId(String charmId);

  ClientUpdateWhere setHisChairLongId(Long hisChairLongId);

  ClientUpdateWhere setHisChairStrId(String hisChairStrId);

  ClientUpdateWhere setLongDescription(String longDescription);

  ClientUpdateWhere setMyChairId1(Long myChairId1);

  ClientUpdateWhere setMyChairId2(String myChairId2);

  ClientUpdateWhere setName(String name);

  ClientUpdateWhere setPatronymic(String patronymic);

  ClientUpdateWhere setSurname(String surname);


  ClientUpdateWhere whereCharmIdIsEqualTo(String charmId);

  ClientUpdateWhere whereHisChairLongIdIsEqualTo(Long hisChairLongId);

  ClientUpdateWhere whereHisChairStrIdIsEqualTo(String hisChairStrId);

  ClientUpdateWhere whereIdIsEqualTo(long id);

  ClientUpdateWhere whereLongDescriptionIsEqualTo(String longDescription);

  ClientUpdateWhere whereMyChairId1IsEqualTo(Long myChairId1);

  ClientUpdateWhere whereMyChairId2IsEqualTo(String myChairId2);

  ClientUpdateWhere whereNameIsEqualTo(String name);

  ClientUpdateWhere wherePatronymicIsEqualTo(String patronymic);

  ClientUpdateWhere whereSurnameIsEqualTo(String surname);

  void commitAll();
}
