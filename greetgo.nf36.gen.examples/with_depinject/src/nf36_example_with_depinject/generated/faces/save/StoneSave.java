package nf36_example_with_depinject.generated.faces.save;

import java.lang.Boolean;
import java.lang.String;
import java.util.function.Predicate;

public interface StoneSave {
  interface name {
    StoneSave set(String value);

    StoneSave skipIf(Predicate<String> predicate);

    StoneSave alias(String alias);
  }

  name name();

  interface actual {
    StoneSave set(boolean value);

    StoneSave skipIf(Predicate<Boolean> predicate);

    StoneSave alias(String alias);
  }

  actual actual();

  void save(Object objectWithData);
}
