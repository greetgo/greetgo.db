package nf36_example_with_depinject.generated.faces.upsert.transaction;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.transaction.TransactionUpsert;

public interface TransactionUpsert {
  TransactionUpsert description(String description);

  TransactionUpsert more(long id);

  void commit();
}
