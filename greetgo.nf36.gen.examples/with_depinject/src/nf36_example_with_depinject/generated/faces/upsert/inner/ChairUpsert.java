package nf36_example_with_depinject.generated.faces.upsert.inner;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.inner.ChairUpsert;

public interface ChairUpsert {
  ChairUpsert name(String name);

  ChairUpsert description(String description);

  ChairUpsert more(long id1, String id2);

  void commit();
}
