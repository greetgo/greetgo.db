package nf36_example_with_depinject.generated.faces;

import nf36_example_with_depinject.generated.faces.history_selector.ClientHistorySelector;
import nf36_example_with_depinject.generated.faces.history_selector.inner.ChairHistorySelector;

public interface ExampleHistorySelector {
  ChairHistorySelector chair();

  ClientHistorySelector client();

}
