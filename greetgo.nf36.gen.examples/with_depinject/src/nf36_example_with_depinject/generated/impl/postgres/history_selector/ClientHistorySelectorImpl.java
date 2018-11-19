package nf36_example_with_depinject.generated.impl.postgres.history_selector;

import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.ClientHistorySelector;
import nf36_example_with_depinject.structure.Client;

import java.util.Date;

public class ClientHistorySelectorImpl implements ClientHistorySelector {

  private final Nf36HistorySelector historySelector;

  public ClientHistorySelectorImpl(Nf36HistorySelector historySelector) {
    this.historySelector = historySelector;
    historySelector.setNf3TableName("client");
    historySelector.setInsertedAtFieldName("created_at");
    historySelector.setTimeFieldName("ts");
    historySelector.addId("id");
  }

  @Override
  public ClientHistorySelector surname() {
    historySelector.field("memory_never_be_superfluous.client_surname", "surname", null);
    return this;
  }

  @Override
  public ClientHistorySelector surnameTo(String surnameAlias) {
    historySelector.field("memory_never_be_superfluous.client_surname", "surname", null);
    historySelector.addFieldAlias("surname", surnameAlias);
    return this;
  }

  @Override
  public ClientHistorySelector name() {
    historySelector.field("memory_never_be_superfluous.client_name", "name", null);
    return this;
  }

  @Override
  public ClientHistorySelector nameTo(String nameAlias) {
    historySelector.field("memory_never_be_superfluous.client_name", "name", null);
    historySelector.addFieldAlias("name", nameAlias);
    return this;
  }

  @Override
  public ClientHistorySelector patronymic() {
    historySelector.field("memory_never_be_superfluous.client_patronymic", "patronymic", null);
    return this;
  }

  @Override
  public ClientHistorySelector patronymicTo(String patronymicAlias) {
    historySelector.field("memory_never_be_superfluous.client_patronymic", "patronymic", patronymicAlias);
    historySelector.addFieldAlias("patronymic", patronymicAlias);
    return this;
  }

  @Override
  public ClientHistorySelector longDescription() {
    historySelector.field("memory_never_be_superfluous.client_long_description", "long_description", null);
    return this;
  }

  @Override
  public ClientHistorySelector longDescriptionTo(String longDescriptionAlias) {
    historySelector.field("memory_never_be_superfluous.client_long_description", "long_description", longDescriptionAlias);
    historySelector.addFieldAlias("long_description", longDescriptionAlias);
    return this;
  }

  private final Finish finish = new Finish() {
    @Override
    public Finish aliasForId(String aliasForId) {
      historySelector.addIdAlias("id", aliasForId);
      return this;
    }

    @Override
    public Client get(long id) {
      Client ret = new Client();
      ret.id = id;
      historySelector.putTo(ret);
      return ret;
    }

    @Override
    public void putTo(Object destinationObject) {
      historySelector.putTo(destinationObject);
    }
  };

  @Override
  public Finish at(Date date) {
    historySelector.at(date);
    return finish;
  }
}
