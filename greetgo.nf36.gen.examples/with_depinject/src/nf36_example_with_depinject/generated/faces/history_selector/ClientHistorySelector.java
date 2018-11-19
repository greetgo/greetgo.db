package nf36_example_with_depinject.generated.faces.history_selector;

import nf36_example_with_depinject.structure.Client;

import java.util.Date;

public interface ClientHistorySelector {

  ClientHistorySelector surname();

  ClientHistorySelector surnameTo(String surnameAlias);

  ClientHistorySelector name();

  ClientHistorySelector nameTo(String nameAlias);

  ClientHistorySelector patronymic();

  ClientHistorySelector patronymicTo(String patronymicAlias);

  ClientHistorySelector longDescription();

  ClientHistorySelector longDescriptionTo(String longDescriptionAlias);

  Finish at(Date date);

  interface Finish {
    Finish aliasForId(String aliasForId);

    void putTo(Object destinationObject);

    Client get(long id);
  }
}
