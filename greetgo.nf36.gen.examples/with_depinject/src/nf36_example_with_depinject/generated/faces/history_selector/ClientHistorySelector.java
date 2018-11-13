package nf36_example_with_depinject.generated.faces.history_selector;

import nf36_example_with_depinject.structure.Client;

import java.util.Date;
import java.util.function.Consumer;

public interface ClientHistorySelector {

  ClientHistorySelector surname();

  ClientHistorySelector surnameTo(String surnameAlias);

  ClientHistorySelector name();

  ClientHistorySelector nameTo(String nameAlias);

  ClientHistorySelector patronymic();

  ClientHistorySelector patronymicTo(String patronymicAlias);

  ClientHistorySelector longDescription();

  ClientHistorySelector longDescriptionTo(String longDescriptionAlias);

  ClientHistorySelector peekSql(Consumer<String> sqlConsumer);

  Finish at(Date date);

  interface Finish {
    Client get(long id);

    Finish aliasForId(String aliasForId);

    void putTo(Object destinationObject);
  }
}
