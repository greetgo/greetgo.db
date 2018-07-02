package nf36_example_with_depinject.generated.faces.update;

import java.lang.String;

public interface StoneUpdate {
  StoneUpdate setActual(boolean actual);

  StoneUpdate setName(String name);


  StoneUpdate whereActualIsEqualTo(boolean actual);

  StoneUpdate whereIdIsEqualTo(String id);

  StoneUpdate whereNameIsEqualTo(String name);

  void commit();
}
