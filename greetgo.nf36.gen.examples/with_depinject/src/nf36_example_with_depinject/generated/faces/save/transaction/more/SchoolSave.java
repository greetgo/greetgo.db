package nf36_example_with_depinject.generated.faces.save.transaction.more;

import java.lang.String;
import java.util.function.Predicate;

public interface SchoolSave {
  interface name {
    SchoolSave set(String value);

    SchoolSave skipIf(Predicate<String> predicate);

    SchoolSave alias(String alias);
  }

  name name();

  void save(Object objectWithData);
}
