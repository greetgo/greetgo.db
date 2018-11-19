package nf36_example_with_depinject.generated.faces.history_selector.inner;

import java.lang.String;
import java.util.Date;
import nf36_example_with_depinject.structure.inner.Chair;

public interface ChairHistorySelector {
  ChairHistorySelector description();

  ChairHistorySelector descriptionInto(String descriptionAlias);

  ChairHistorySelector name();

  ChairHistorySelector nameInto(String nameAlias);

  Finish at(Date at);

  interface Finish {
    Finish aliasForId1(String aliasForId1);

    Finish aliasForId2(String aliasForId2);

    void putTo(Object destinationObject);

    Chair get(long id1, String id2);

  }
}
