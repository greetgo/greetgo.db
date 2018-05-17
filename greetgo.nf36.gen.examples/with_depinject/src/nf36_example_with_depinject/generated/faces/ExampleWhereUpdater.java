package nf36_example_with_depinject.generated.faces;

import nf36_example_with_depinject.generated.faces.update_where.ClientUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.PersonUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.StoneUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.StreetUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.ChairUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.CharmUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.ClientAddressUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.WowUpdateWhere;

public interface ExampleWhereUpdater {
  ChairUpdateWhere chair();

  CharmUpdateWhere charm();

  ClientUpdateWhere client();

  ClientAddressUpdateWhere clientAddress();

  PersonUpdateWhere person();

  StoneUpdateWhere stone();

  StreetUpdateWhere street();

  WowUpdateWhere wow();

}
