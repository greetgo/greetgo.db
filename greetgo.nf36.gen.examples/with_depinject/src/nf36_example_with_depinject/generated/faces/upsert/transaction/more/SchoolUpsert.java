package nf36_example_with_depinject.generated.faces.upsert.transaction.more;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.transaction.more.SchoolUpsert;

public interface SchoolUpsert {
  SchoolUpsert name(String name);

  SchoolUpsert more(String id);

  void commit();
}
