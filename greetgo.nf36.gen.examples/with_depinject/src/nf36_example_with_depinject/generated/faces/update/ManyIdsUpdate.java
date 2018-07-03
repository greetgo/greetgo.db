package nf36_example_with_depinject.generated.faces.update;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

public interface ManyIdsUpdate {
  ManyIdsUpdate setAField(int aField);


  ManyIdsUpdate whereAFieldIsEqualTo(int aField);

  ManyIdsUpdate whereBoxedIntIdIsEqualTo(Integer boxedIntId);

  ManyIdsUpdate whereBoxedLongIdIsEqualTo(Long boxedLongId);

  ManyIdsUpdate whereIntIdIsEqualTo(int intId);

  ManyIdsUpdate whereLongIdIsEqualTo(long longId);

  ManyIdsUpdate whereStrIdIsEqualTo(String strId);

  void commit();
}
