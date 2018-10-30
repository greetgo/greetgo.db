package nf36_example_with_depinject.generated.faces.save.transaction;

import java.lang.String;
import java.util.function.Predicate;

public interface TransactionSave {
  interface description {
    TransactionSave set(String value);

    TransactionSave skipIf(Predicate<String> predicate);

    TransactionSave alias(String alias);
  }

  description description();

  void save(Object objectWithData);
}
