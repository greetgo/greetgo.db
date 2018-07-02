package nf36_example_with_depinject.generated.faces.update;

import java.lang.String;
import java.math.BigDecimal;

public interface PersonUpdate {
  PersonUpdate setAmount(BigDecimal amount);

  PersonUpdate setAmountRU(BigDecimal amountRU);

  PersonUpdate setBlocked(boolean blocked);

  PersonUpdate setFio(String fio);


  PersonUpdate whereAmountIsEqualTo(BigDecimal amount);

  PersonUpdate whereAmountRUIsEqualTo(BigDecimal amountRU);

  PersonUpdate whereBlockedIsEqualTo(boolean blocked);

  PersonUpdate whereFioIsEqualTo(String fio);

  PersonUpdate whereIdIsEqualTo(String id);

  void commit();
}
