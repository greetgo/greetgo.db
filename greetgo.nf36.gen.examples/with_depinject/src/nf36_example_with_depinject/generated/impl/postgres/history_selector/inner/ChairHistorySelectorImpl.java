package nf36_example_with_depinject.generated.impl.postgres.history_selector.inner;

import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.inner.ChairHistorySelector;
import nf36_example_with_depinject.structure.inner.Chair;

import java.util.Date;

public class ChairHistorySelectorImpl implements ChairHistorySelector {

  private final Nf36HistorySelector historySelector;

  public ChairHistorySelectorImpl(Nf36HistorySelector historySelector) {
    this.historySelector = historySelector;
    historySelector.setNf3TableName("chair");
    historySelector.addId("id1");
    historySelector.addId("id2");
  }

  @Override
  public ChairHistorySelector name() {
    historySelector.field("memory_never_be_superfluous.chair_name", "name", null);
    return this;
  }

  @Override
  public ChairHistorySelector nameTo(String nameAlias) {
    historySelector.field("memory_never_be_superfluous.chair_name", "name", nameAlias);
    return this;
  }

  @Override
  public ChairHistorySelector description() {
    historySelector.field("memory_never_be_superfluous.chair_description", "description", null);
    return this;
  }

  @Override
  public ChairHistorySelector descriptionTo(String descriptionAlias) {
    historySelector.field("memory_never_be_superfluous.chair_description", "description", descriptionAlias);
    return this;
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
    public Chair get(long id1, String id2) {
      Chair ret = new Chair();
      ret.id1 = id1;
      ret.id2 = id2;
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
