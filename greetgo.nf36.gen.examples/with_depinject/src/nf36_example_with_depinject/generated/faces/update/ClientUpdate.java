package nf36_example_with_depinject.generated.faces.update;

import java.lang.Long;
import java.lang.String;

public interface ClientUpdate {
  ClientUpdate setCharmId(String charmId);

  ClientUpdate setHisChairLongId(Long hisChairLongId);

  ClientUpdate setHisChairStrId(String hisChairStrId);

  ClientUpdate setLongDescription(String longDescription);

  ClientUpdate setMyChairId1(Long myChairId1);

  ClientUpdate setMyChairId2(String myChairId2);

  ClientUpdate setName(String name);

  ClientUpdate setPatronymic(String patronymic);

  ClientUpdate setSurname(String surname);


  ClientUpdate whereCharmIdIsEqualTo(String charmId);

  ClientUpdate whereHisChairLongIdIsEqualTo(Long hisChairLongId);

  ClientUpdate whereHisChairStrIdIsEqualTo(String hisChairStrId);

  ClientUpdate whereIdIsEqualTo(long id);

  ClientUpdate whereLongDescriptionIsEqualTo(String longDescription);

  ClientUpdate whereMyChairId1IsEqualTo(Long myChairId1);

  ClientUpdate whereMyChairId2IsEqualTo(String myChairId2);

  ClientUpdate whereNameIsEqualTo(String name);

  ClientUpdate wherePatronymicIsEqualTo(String patronymic);

  ClientUpdate whereSurnameIsEqualTo(String surname);

  void commitAll();
}
