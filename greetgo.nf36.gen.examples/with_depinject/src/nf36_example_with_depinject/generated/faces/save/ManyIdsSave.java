package nf36_example_with_depinject.generated.faces.save;

import java.lang.Integer;
import java.util.function.Predicate;

public interface ManyIdsSave {
  interface aField {
    ManyIdsSave set(int value);

    ManyIdsSave skipIf(Predicate<Integer> predicate);

    ManyIdsSave alias(String alias);
  }

  aField aField();

  void save(Object objectWithData);
}
