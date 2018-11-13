package nf36_example_with_depinject.generated.impl.postgres;

import kz.greetgo.db.nf36.core.Nf36HistorySelector;
import nf36_example_with_depinject.generated.faces.ExampleHistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.ClientHistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.inner.ChairHistorySelector;
import nf36_example_with_depinject.generated.impl.postgres.history_selector.ClientHistorySelectorImpl;
import nf36_example_with_depinject.generated.impl.postgres.history_selector.inner.ChairHistorySelectorImpl;

public abstract class AbstractExampleHistorySelectorPostgres implements ExampleHistorySelector {

  protected abstract Nf36HistorySelector createHistorySelector();

  @Override
  public ClientHistorySelector client() {
    return new ClientHistorySelectorImpl(createHistorySelector());
  }

  @Override
  public ChairHistorySelector chair() {
    return new ChairHistorySelectorImpl(createHistorySelector());
  }
}
