package nf36_example_with_depinject.generated.faces.save;

import java.lang.String;
import java.util.function.Predicate;
import nf36_example_with_depinject.structure.StreetType;

public interface StreetSave {
  interface type {
    StreetSave set(StreetType value);

    StreetSave skipIf(Predicate<StreetType> predicate);

    StreetSave alias(String alias);
  }

  type type();

  interface name {
    StreetSave set(String value);

    StreetSave skipIf(Predicate<String> predicate);

    StreetSave alias(String alias);
  }

  name name();

  void save(Object objectWithData);
}
