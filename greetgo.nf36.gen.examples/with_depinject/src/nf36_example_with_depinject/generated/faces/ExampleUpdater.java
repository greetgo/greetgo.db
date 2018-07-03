package nf36_example_with_depinject.generated.faces;

import nf36_example_with_depinject.generated.faces.update.ClientUpdate;
import nf36_example_with_depinject.generated.faces.update.ManyIdsUpdate;
import nf36_example_with_depinject.generated.faces.update.PersonUpdate;
import nf36_example_with_depinject.generated.faces.update.StoneUpdate;
import nf36_example_with_depinject.generated.faces.update.StreetUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.ChairUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.CharmUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.ClientAddressUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.WowUpdate;

public interface ExampleUpdater {
  ChairUpdate chair();

  CharmUpdate charm();

  ClientUpdate client();

  ClientAddressUpdate clientAddress();

  ManyIdsUpdate manyIds();

  PersonUpdate person();

  StoneUpdate stone();

  StreetUpdate street();

  WowUpdate wow();

}
