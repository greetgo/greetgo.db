package nf36_example_with_depinject.generated.faces.update.transaction;

import java.lang.String;

public interface TransactionUpdate {
  TransactionUpdate setDescription(String description);


  TransactionUpdate whereDescriptionIsEqualTo(String description);

  TransactionUpdate whereIdIsEqualTo(long id);

  void commit();
}
