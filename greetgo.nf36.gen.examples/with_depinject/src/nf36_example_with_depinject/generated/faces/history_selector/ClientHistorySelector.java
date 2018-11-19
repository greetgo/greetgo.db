package nf36_example_with_depinject.generated.faces.history_selector;

import java.util.Date;
import nf36_example_with_depinject.structure.Client;

public interface ClientHistorySelector {
  ClientHistorySelector charmId();

  ClientHistorySelector charmIdTo(String charmIdAlias);

  ClientHistorySelector hisChairLongId();

  ClientHistorySelector hisChairLongIdTo(String hisChairLongIdAlias);

  ClientHistorySelector hisChairStrId();

  ClientHistorySelector hisChairStrIdTo(String hisChairStrIdAlias);

  ClientHistorySelector longDescription();

  ClientHistorySelector longDescriptionTo(String longDescriptionAlias);

  ClientHistorySelector myChairId1();

  ClientHistorySelector myChairId1To(String myChairId1Alias);

  ClientHistorySelector myChairId2();

  ClientHistorySelector myChairId2To(String myChairId2Alias);

  ClientHistorySelector name();

  ClientHistorySelector nameTo(String nameAlias);

  ClientHistorySelector patronymic();

  ClientHistorySelector patronymicTo(String patronymicAlias);

  ClientHistorySelector surname();

  ClientHistorySelector surnameTo(String surnameAlias);

  Finish atMoment(Date at);

  interface Finish {
    Finish aliasForId(String aliasForId);

    void putTo(Object destinationObject);

    Client get(long id);

  }
}
