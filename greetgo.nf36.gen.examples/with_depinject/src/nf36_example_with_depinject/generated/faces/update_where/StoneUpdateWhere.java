package nf36_example_with_depinject.generated.faces.update_where;

import java.lang.String;

public interface StoneUpdateWhere {
  StoneUpdateWhere setActual(boolean actual);

  StoneUpdateWhere setName(String name);


  StoneUpdateWhere whereActualIsEqualTo(boolean actual);

  StoneUpdateWhere whereIdIsEqualTo(String id);

  StoneUpdateWhere whereNameIsEqualTo(String name);

  void commit();
}
