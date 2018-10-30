package nf36_example_with_depinject.generated.faces.save.inner;

import java.lang.String;
import java.util.function.Predicate;

public interface ChairSave {
  interface name {
    ChairSave set(String value);

    ChairSave skipIf(Predicate<String> predicate);

    ChairSave alias(String alias);
  }

  name name();

  interface description {
    ChairSave set(String value);

    ChairSave skipIf(Predicate<String> predicate);

    ChairSave alias(String alias);
  }

  description description();

  void save(Object objectWithData);
}
