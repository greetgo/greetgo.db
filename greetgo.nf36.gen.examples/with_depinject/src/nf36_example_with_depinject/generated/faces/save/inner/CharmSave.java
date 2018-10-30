package nf36_example_with_depinject.generated.faces.save.inner;

import java.lang.String;
import java.util.function.Predicate;

public interface CharmSave {
  interface name {
    CharmSave set(String value);

    CharmSave skipIf(Predicate<String> predicate);

    CharmSave alias(String alias);
  }

  name name();

  void save(Object objectWithData);
}
