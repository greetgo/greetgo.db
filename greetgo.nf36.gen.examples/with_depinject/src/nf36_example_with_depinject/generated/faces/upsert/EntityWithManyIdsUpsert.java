package nf36_example_with_depinject.generated.faces.upsert;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.EntityWithManyIdsUpsert;

public interface EntityWithManyIdsUpsert {
  EntityWithManyIdsUpsert aField(int aField);

  EntityWithManyIdsUpsert more(int intId, Integer boxedIntId, long longId, Long boxedLongId, String strId);

  void commit();
}
