package kz.greetgo.db.nf36.gen.example.generated.faces.upsert;

import java.lang.String;
import java.math.BigDecimal;

public interface PersonUpsert {
  PersonUpsert fio(String fio);

  PersonUpsert blocked(boolean blocked);

  PersonUpsert amount(BigDecimal amount);

  void commit();
}
