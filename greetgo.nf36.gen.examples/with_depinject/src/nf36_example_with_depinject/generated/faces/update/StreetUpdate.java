package nf36_example_with_depinject.generated.faces.update;

import java.lang.String;
import nf36_example_with_depinject.structure.StreetType;

public interface StreetUpdate {
  StreetUpdate setName(String name);

  StreetUpdate setType(StreetType type);


  StreetUpdate whereIdIsEqualTo(long id);

  StreetUpdate whereNameIsEqualTo(String name);

  StreetUpdate whereTypeIsEqualTo(StreetType type);

  void commit();
}
