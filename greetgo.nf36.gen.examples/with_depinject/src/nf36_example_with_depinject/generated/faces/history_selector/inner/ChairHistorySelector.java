package nf36_example_with_depinject.generated.faces.history_selector.inner;

import nf36_example_with_depinject.structure.inner.Chair;

import java.util.Date;
import java.util.function.Consumer;

public interface ChairHistorySelector {

  ChairHistorySelector name();

  ChairHistorySelector nameTo(String nameAlias);

  ChairHistorySelector description();

  ChairHistorySelector descriptionTo(String descriptionAlias);

  ChairHistorySelector peekSql(Consumer<String> sqlConsumer);

  Finish at(Date date);

  interface Finish {
    Chair get(long id1, String id2);

    Finish aliasForId1(String aliasForId1);

    Finish aliasForId2(String aliasForId2);

    void putTo(Object destinationObject);
  }
}
