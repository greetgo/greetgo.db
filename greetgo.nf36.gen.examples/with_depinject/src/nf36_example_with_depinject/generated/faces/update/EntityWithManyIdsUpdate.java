package nf36_example_with_depinject.generated.faces.update;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

public interface EntityWithManyIdsUpdate {
  EntityWithManyIdsUpdate setAField(int aField);


  EntityWithManyIdsUpdate whereAFieldIsEqualTo(int aField);

  EntityWithManyIdsUpdate whereBoxedIntIdIsEqualTo(Integer boxedIntId);

  EntityWithManyIdsUpdate whereBoxedLongIdIsEqualTo(Long boxedLongId);

  EntityWithManyIdsUpdate whereIntIdIsEqualTo(int intId);

  EntityWithManyIdsUpdate whereLongIdIsEqualTo(long longId);

  EntityWithManyIdsUpdate whereStrIdIsEqualTo(String strId);

  void commit();
}
