package nf36_example_with_depinject.generated.faces.save;

import java.lang.Boolean;
import java.lang.String;
import java.math.BigDecimal;
import java.util.function.Predicate;

public interface PersonSave {
  interface fio {
    PersonSave set(String value);

    PersonSave skipIf(Predicate<String> predicate);

    PersonSave alias(String alias);
  }

  fio fio();

  interface blocked {
    PersonSave set(boolean value);

    PersonSave skipIf(Predicate<Boolean> predicate);

    PersonSave alias(String alias);
  }

  blocked blocked();

  interface amount {
    PersonSave set(BigDecimal value);

    PersonSave skipIf(Predicate<BigDecimal> predicate);

    PersonSave alias(String alias);
  }

  amount amount();

  interface amountRU {
    PersonSave set(BigDecimal value);

    PersonSave skipIf(Predicate<BigDecimal> predicate);

    PersonSave alias(String alias);
  }

  amountRU amountRU();

  void save(Object objectWithData);
}
