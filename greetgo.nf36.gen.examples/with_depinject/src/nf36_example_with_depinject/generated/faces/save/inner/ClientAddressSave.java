package nf36_example_with_depinject.generated.faces.save.inner;

import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.function.Predicate;
import nf36_example_with_depinject.structure.AddressType;

public interface ClientAddressSave {
  interface type {
    ClientAddressSave set(AddressType value);

    ClientAddressSave skipIf(Predicate<AddressType> predicate);

    ClientAddressSave alias(String alias);
  }

  type type();

  interface streetId {
    ClientAddressSave set(long value);

    ClientAddressSave skipIf(Predicate<Long> predicate);

    ClientAddressSave alias(String alias);
  }

  streetId streetId();

  interface house {
    ClientAddressSave set(String value);

    ClientAddressSave skipIf(Predicate<String> predicate);

    ClientAddressSave alias(String alias);
  }

  house house();

  interface flat {
    ClientAddressSave set(String value);

    ClientAddressSave skipIf(Predicate<String> predicate);

    ClientAddressSave alias(String alias);
  }

  flat flat();

  interface birthDate {
    ClientAddressSave set(Date value);

    ClientAddressSave skipIf(Predicate<Date> predicate);

    ClientAddressSave alias(String alias);
  }

  birthDate birthDate();

  void save(Object objectWithData);
}
