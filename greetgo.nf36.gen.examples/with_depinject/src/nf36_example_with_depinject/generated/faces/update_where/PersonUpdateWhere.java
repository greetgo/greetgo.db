package nf36_example_with_depinject.generated.faces.update_where;

import java.lang.String;
import java.math.BigDecimal;

public interface PersonUpdateWhere {
  PersonUpdateWhere setAmount(BigDecimal amount);

  PersonUpdateWhere setBlocked(boolean blocked);

  PersonUpdateWhere setFio(String fio);


  PersonUpdateWhere whereAmountIsEqualTo(BigDecimal amount);

  PersonUpdateWhere whereBlockedIsEqualTo(boolean blocked);

  PersonUpdateWhere whereFioIsEqualTo(String fio);

  PersonUpdateWhere whereIdIsEqualTo(String id);

  void commit();
}
