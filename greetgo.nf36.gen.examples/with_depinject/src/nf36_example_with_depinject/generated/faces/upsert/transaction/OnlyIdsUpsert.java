package nf36_example_with_depinject.generated.faces.upsert.transaction;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.transaction.OnlyIdsUpsert;

public interface OnlyIdsUpsert {
  OnlyIdsUpsert more(long id1, String id2);

  void commit();
}
