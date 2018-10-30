package nf36_example_with_depinject.generated.faces.save.inner;

import java.lang.String;
import java.util.function.Predicate;

public interface WowSave {
  interface hello {
    WowSave set(String value);

    WowSave skipIf(Predicate<String> predicate);

    WowSave alias(String alias);
  }

  hello hello();

  void save(Object objectWithData);
}
