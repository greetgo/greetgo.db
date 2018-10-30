package nf36_example_with_depinject.generated.faces.save;

import java.lang.String;
import java.util.function.Predicate;

public interface EntityEnumAsIdSave {
  interface value {
    EntityEnumAsIdSave set(String value);

    EntityEnumAsIdSave skipIf(Predicate<String> predicate);

    EntityEnumAsIdSave alias(String alias);
  }

  value value();

  void save(Object objectWithData);
}
