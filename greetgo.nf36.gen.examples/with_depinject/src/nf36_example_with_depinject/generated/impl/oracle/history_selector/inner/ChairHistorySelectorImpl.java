package nf36_example_with_depinject.generated.impl.oracle.history_selector.inner;

import java.lang.String;
import java.util.Date;
import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.inner.ChairHistorySelector;
import nf36_example_with_depinject.structure.inner.Chair;

public class ChairHistorySelectorImpl implements ChairHistorySelector {
  private final Nf36HistorySelector historySelector;

  public ChairHistorySelectorImpl(Nf36HistorySelector historySelector) {

    this.historySelector = historySelector;
    historySelector.setNf3TableName("chair");
    historySelector.setTimeFieldName("ts");
    historySelector.setInsertedAtFieldName("created_at");
    historySelector.addId("id1");
    historySelector.addId("id2");
  }

  @Override
  public ChairHistorySelector description() {
    historySelector.field("m_chair_description", "description", null);
    return this;
  }

  @Override
  public ChairHistorySelector descriptionInto(String descriptionAlias) {
    historySelector.field("m_chair_description", "description", null);
    historySelector.addFieldAlias("description", descriptionAlias);
    return this;
  }

  @Override
  public ChairHistorySelector name() {
    historySelector.field("m_chair_name", "name", null);
    return this;
  }

  @Override
  public ChairHistorySelector nameInto(String nameAlias) {
    historySelector.field("m_chair_name", "name", null);
    historySelector.addFieldAlias("name", nameAlias);
    return this;
  }

  @Override
  public Finish at(Date at) {
    historySelector.at(at);
    return finish;
  }

  private final Finish finish = new Finish() {
    @Override
    public Finish aliasForId1(String aliasForId1) {
      historySelector.addIdAlias("id1", aliasForId1);
      return this;
    }

    @Override
    public Finish aliasForId2(String aliasForId2) {
      historySelector.addIdAlias("id2", aliasForId2);
      return this;
    }

    @Override
    public void putTo(Object destinationObject) {
      historySelector.putTo(destinationObject);
    }

    @Override
    public Chair get(long id1, String id2) {
      Chair ret = new Chair();
      ret.id1 = id1;
      ret.id2 = id2;
      historySelector.putTo(ret);
      return ret;
    }
  };
}
