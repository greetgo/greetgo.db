package nf36_example_with_depinject.generated.impl.oracle;

import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.ExampleHistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.ClientHistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.inner.ChairHistorySelector;
import nf36_example_with_depinject.generated.impl.oracle.history_selector.ClientHistorySelectorImpl;
import nf36_example_with_depinject.generated.impl.oracle.history_selector.inner.ChairHistorySelectorImpl;

public abstract class AbstractExampleHistorySelectorOracle implements ExampleHistorySelector {
  protected abstract Nf36HistorySelector createHistorySelector();

  @Override
  public ChairHistorySelector chair() {
    return new ChairHistorySelectorImpl(createHistorySelector());
  }

  @Override
  public ClientHistorySelector client() {
    return new ClientHistorySelectorImpl(createHistorySelector());
  }

}
