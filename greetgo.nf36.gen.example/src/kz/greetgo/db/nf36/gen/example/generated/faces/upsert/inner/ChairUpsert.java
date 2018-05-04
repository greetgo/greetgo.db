package kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.ChairUpsert;

public interface ChairUpsert {
  ChairUpsert name(String name);

  ChairUpsert description(String description);

  ChairUpsert more(long id1, String id2);

  void commit();
}
