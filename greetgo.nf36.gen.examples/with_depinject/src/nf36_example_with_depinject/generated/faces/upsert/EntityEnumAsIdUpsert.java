package nf36_example_with_depinject.generated.faces.upsert;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.EntityEnumAsIdUpsert;
import nf36_example_with_depinject.structure.SomeEnum;

public interface EntityEnumAsIdUpsert {
  EntityEnumAsIdUpsert value(String value);

  EntityEnumAsIdUpsert more(SomeEnum id);

  void commit();
}
