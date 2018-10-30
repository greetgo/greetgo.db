package nf36_example_with_depinject.generated.faces.save;

import java.util.function.Predicate;

public interface ClientSave {

  interface surname {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  surname surname();

  interface myChairId1 {
    ClientSave set(Long value);

    ClientSave skipIf(Predicate<Long> predicate);

    ClientSave alias(String alias);
  }

  myChairId1 myChairId1();

  void save(Object objectWithData);
}
