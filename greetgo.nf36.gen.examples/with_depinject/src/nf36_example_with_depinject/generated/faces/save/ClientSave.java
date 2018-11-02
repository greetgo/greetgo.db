package nf36_example_with_depinject.generated.faces.save;

import java.lang.Long;
import java.lang.String;
import java.util.function.Predicate;

public interface ClientSave {
  interface surname {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  surname surname();

  interface name {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  name name();

  interface patronymic {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  patronymic patronymic();

  interface charmId {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  charmId charmId();

  interface longDescription {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  longDescription longDescription();

  interface myChairId1 {
    ClientSave set(Long value);

    ClientSave skipIf(Predicate<Long> predicate);

    ClientSave alias(String alias);
  }

  myChairId1 myChairId1();

  interface myChairId2 {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  myChairId2 myChairId2();

  interface hisChairLongId {
    ClientSave set(Long value);

    ClientSave skipIf(Predicate<Long> predicate);

    ClientSave alias(String alias);
  }

  hisChairLongId hisChairLongId();

  interface hisChairStrId {
    ClientSave set(String value);

    ClientSave skipIf(Predicate<String> predicate);

    ClientSave alias(String alias);
  }

  hisChairStrId hisChairStrId();

  void saveAll(Object objectWithData);
}
