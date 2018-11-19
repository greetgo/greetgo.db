package nf36_example_with_depinject.generated.impl.oracle.history_selector;

import java.util.Date;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.ClientHistorySelector;
import nf36_example_with_depinject.structure.Client;

public class ClientHistorySelectorImpl implements ClientHistorySelector {
  private final Nf36HistorySelector historySelector;

  public ClientHistorySelectorImpl(Nf36HistorySelector historySelector) {

    this.historySelector = historySelector;
    historySelector.setNf3TableName("client");
    historySelector.setTimeFieldName("ts");
    historySelector.setInsertedAtFieldName("created_at");
    historySelector.addId("id");
  }

  @Override
  public ClientHistorySelector charmId() {
    historySelector.field("m_client_charm_id", "charm_id", null);
    return this;
  }

  @Override
  public ClientHistorySelector charmIdTo(String charmIdAlias) {
    historySelector.field("m_client_charm_id", "charm_id", null);
    historySelector.addFieldAlias("charm_id", charmIdAlias);
    return this;
  }

  @Override
  public ClientHistorySelector hisChairLongId() {
    historySelector.field("m_client_his_chair_long_id", "his_chair_long_id", null);
    return this;
  }

  @Override
  public ClientHistorySelector hisChairLongIdTo(String hisChairLongIdAlias) {
    historySelector.field("m_client_his_chair_long_id", "his_chair_long_id", null);
    historySelector.addFieldAlias("his_chair_long_id", hisChairLongIdAlias);
    return this;
  }

  @Override
  public ClientHistorySelector hisChairStrId() {
    historySelector.field("m_client_his_chair_long_id", "his_chair_str_id", null);
    return this;
  }

  @Override
  public ClientHistorySelector hisChairStrIdTo(String hisChairStrIdAlias) {
    historySelector.field("m_client_his_chair_long_id", "his_chair_str_id", null);
    historySelector.addFieldAlias("his_chair_str_id", hisChairStrIdAlias);
    return this;
  }

  @Override
  public ClientHistorySelector longDescription() {
    historySelector.field("m_client_long_description", "long_description", null);
    return this;
  }

  @Override
  public ClientHistorySelector longDescriptionTo(String longDescriptionAlias) {
    historySelector.field("m_client_long_description", "long_description", null);
    historySelector.addFieldAlias("long_description", longDescriptionAlias);
    return this;
  }

  @Override
  public ClientHistorySelector myChairId1() {
    historySelector.field("m_client_my_chair_id1", "my_chair_id1", null);
    return this;
  }

  @Override
  public ClientHistorySelector myChairId1To(String myChairId1Alias) {
    historySelector.field("m_client_my_chair_id1", "my_chair_id1", null);
    historySelector.addFieldAlias("my_chair_id1", myChairId1Alias);
    return this;
  }

  @Override
  public ClientHistorySelector myChairId2() {
    historySelector.field("m_client_my_chair_id1", "my_chair_id2", null);
    return this;
  }

  @Override
  public ClientHistorySelector myChairId2To(String myChairId2Alias) {
    historySelector.field("m_client_my_chair_id1", "my_chair_id2", null);
    historySelector.addFieldAlias("my_chair_id2", myChairId2Alias);
    return this;
  }

  @Override
  public ClientHistorySelector name() {
    historySelector.field("m_client_name", "name", null);
    return this;
  }

  @Override
  public ClientHistorySelector nameTo(String nameAlias) {
    historySelector.field("m_client_name", "name", null);
    historySelector.addFieldAlias("name", nameAlias);
    return this;
  }

  @Override
  public ClientHistorySelector patronymic() {
    historySelector.field("m_client_patronymic", "patronymic", null);
    return this;
  }

  @Override
  public ClientHistorySelector patronymicTo(String patronymicAlias) {
    historySelector.field("m_client_patronymic", "patronymic", null);
    historySelector.addFieldAlias("patronymic", patronymicAlias);
    return this;
  }

  @Override
  public ClientHistorySelector surname() {
    historySelector.field("m_client_surname", "surname", null);
    return this;
  }

  @Override
  public ClientHistorySelector surnameTo(String surnameAlias) {
    historySelector.field("m_client_surname", "surname", null);
    historySelector.addFieldAlias("surname", surnameAlias);
    return this;
  }

  @Override
  public Finish atMoment(Date at) {
    historySelector.at(at);
    return finish;
  }

  private final Finish finish = new Finish() {
    @Override
    public Finish aliasForId(String aliasForId) {
      historySelector.addIdAlias("id", aliasForId);
      return this;
    }

    @Override
    public void putTo(Object destinationObject) {
      historySelector.putTo(destinationObject);
    }

    @Override
    public Client get(long id) {
      Client ret = new Client();
      ret.id = id;
      historySelector.putTo(ret);
      return ret;
    }
  };
}
