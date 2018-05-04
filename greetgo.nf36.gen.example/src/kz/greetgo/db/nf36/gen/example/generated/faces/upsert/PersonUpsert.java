package kz.greetgo.db.nf36.gen.example.generated.faces.upsert;

import java.lang.String;
import java.math.BigDecimal;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.PersonUpsert;

public interface PersonUpsert {
  PersonUpsert fio(String fio);

  PersonUpsert blocked(boolean blocked);

  PersonUpsert amount(BigDecimal amount);

  PersonUpsert more(String id);

  void commit();
}
