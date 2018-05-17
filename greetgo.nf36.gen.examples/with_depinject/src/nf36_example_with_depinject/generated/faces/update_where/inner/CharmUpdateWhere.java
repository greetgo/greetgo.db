package nf36_example_with_depinject.generated.faces.update_where.inner;

import java.lang.String;

public interface CharmUpdateWhere {
  CharmUpdateWhere setName(String name);


  CharmUpdateWhere whereIdIsEqualTo(String id);

  CharmUpdateWhere whereNameIsEqualTo(String name);

  void commit();
}
