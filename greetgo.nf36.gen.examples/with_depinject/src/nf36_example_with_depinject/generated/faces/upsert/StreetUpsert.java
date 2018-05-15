package nf36_example_with_depinject.generated.faces.upsert;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.StreetUpsert;
import nf36_example_with_depinject.structure.StreetType;

public interface StreetUpsert {
  StreetUpsert type(StreetType type);

  StreetUpsert name(String name);

  StreetUpsert more(long id);

  void commit();
}
