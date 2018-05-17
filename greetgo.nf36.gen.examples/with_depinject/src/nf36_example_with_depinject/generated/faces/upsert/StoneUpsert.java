package nf36_example_with_depinject.generated.faces.upsert;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.StoneUpsert;

public interface StoneUpsert {
  StoneUpsert name(String name);

  StoneUpsert actual(boolean actual);

  StoneUpsert more(String id);

  void commit();
}
