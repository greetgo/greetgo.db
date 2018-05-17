package nf36_example_with_depinject.generated.faces.update_where.inner;

import java.lang.String;

public interface ChairUpdateWhere {
  ChairUpdateWhere setDescription(String description);

  ChairUpdateWhere setName(String name);


  ChairUpdateWhere whereDescriptionIsEqualTo(String description);

  ChairUpdateWhere whereId1IsEqualTo(long id1);

  ChairUpdateWhere whereId2IsEqualTo(String id2);

  ChairUpdateWhere whereNameIsEqualTo(String name);

  void commit();
}
