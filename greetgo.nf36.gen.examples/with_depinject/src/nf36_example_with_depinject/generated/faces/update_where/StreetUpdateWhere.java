package nf36_example_with_depinject.generated.faces.update_where;

import java.lang.String;
import nf36_example_with_depinject.structure.StreetType;

public interface StreetUpdateWhere {
  StreetUpdateWhere setName(String name);

  StreetUpdateWhere setType(StreetType type);


  StreetUpdateWhere whereIdIsEqualTo(long id);

  StreetUpdateWhere whereNameIsEqualTo(String name);

  StreetUpdateWhere whereTypeIsEqualTo(StreetType type);

  void commit();
}
