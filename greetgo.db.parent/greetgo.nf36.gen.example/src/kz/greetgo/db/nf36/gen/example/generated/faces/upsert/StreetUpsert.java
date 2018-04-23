package kz.greetgo.db.nf36.gen.example.generated.faces.upsert;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.structure.StreetType;

public interface StreetUpsert {
  StreetUpsert type(StreetType type);

  StreetUpsert name(String name);

  void commit();
}
