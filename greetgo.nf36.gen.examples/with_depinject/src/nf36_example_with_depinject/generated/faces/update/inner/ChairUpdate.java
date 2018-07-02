package nf36_example_with_depinject.generated.faces.update.inner;

import java.lang.String;

public interface ChairUpdate {
  ChairUpdate setDescription(String description);

  ChairUpdate setName(String name);


  ChairUpdate whereDescriptionIsEqualTo(String description);

  ChairUpdate whereId1IsEqualTo(long id1);

  ChairUpdate whereId2IsEqualTo(String id2);

  ChairUpdate whereNameIsEqualTo(String name);

  void commit();
}
