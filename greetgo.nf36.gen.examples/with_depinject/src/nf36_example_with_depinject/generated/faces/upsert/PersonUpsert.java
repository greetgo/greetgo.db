package nf36_example_with_depinject.generated.faces.upsert;

import java.lang.String;
import java.math.BigDecimal;
import nf36_example_with_depinject.generated.faces.upsert.PersonUpsert;

public interface PersonUpsert {
  PersonUpsert fio(String fio);

  PersonUpsert blocked(boolean blocked);

  PersonUpsert amount(BigDecimal amount);

  PersonUpsert amountRU(BigDecimal amountRU);

  PersonUpsert more(String id);

  void commit();
}
