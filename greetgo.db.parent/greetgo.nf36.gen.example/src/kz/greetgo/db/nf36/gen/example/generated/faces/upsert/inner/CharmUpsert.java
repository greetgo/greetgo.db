package kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.CharmUpsert;

public interface CharmUpsert {
  CharmUpsert name(String name);

  CharmUpsert more(String id);

  void commit();
}
