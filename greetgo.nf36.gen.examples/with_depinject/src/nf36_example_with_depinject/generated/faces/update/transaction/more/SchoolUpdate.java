package nf36_example_with_depinject.generated.faces.update.transaction.more;

import java.lang.String;

public interface SchoolUpdate {
  SchoolUpdate setName(String name);


  SchoolUpdate whereIdIsEqualTo(String id);

  SchoolUpdate whereNameIsEqualTo(String name);

  void commit();
}
