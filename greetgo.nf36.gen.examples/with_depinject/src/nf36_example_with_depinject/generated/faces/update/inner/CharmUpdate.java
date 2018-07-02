package nf36_example_with_depinject.generated.faces.update.inner;

import java.lang.String;

public interface CharmUpdate {
  CharmUpdate setName(String name);


  CharmUpdate whereIdIsEqualTo(String id);

  CharmUpdate whereNameIsEqualTo(String name);

  void commit();
}
