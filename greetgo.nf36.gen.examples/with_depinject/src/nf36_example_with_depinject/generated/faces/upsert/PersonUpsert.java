package nf36_example_with_depinject.generated.faces.upsert;

import java.math.BigDecimal;

public interface PersonUpsert {
  PersonUpsert fio(String fio);

  PersonUpsert blocked(boolean blocked);

  PersonUpsert amount(BigDecimal amount);

  PersonUpsert more(String id);

  void commit();
}
