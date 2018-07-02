package nf36_example_with_depinject.generated.impl.postgres;

import kz.greetgo.db.nf36.core.Nf36Updater;
import nf36_example_with_depinject.generated.faces.ExampleUpdater;
import nf36_example_with_depinject.generated.faces.update.ClientUpdate;
import nf36_example_with_depinject.generated.faces.update.PersonUpdate;
import nf36_example_with_depinject.generated.faces.update.StoneUpdate;
import nf36_example_with_depinject.generated.faces.update.StreetUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.ChairUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.CharmUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.ClientAddressUpdate;
import nf36_example_with_depinject.generated.faces.update.inner.WowUpdate;
import nf36_example_with_depinject.generated.impl.postgres.update.ClientUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.PersonUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.StoneUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.StreetUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.inner.ChairUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.inner.CharmUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.inner.ClientAddressUpdateImpl;
import nf36_example_with_depinject.generated.impl.postgres.update.inner.WowUpdateImpl;

public abstract class AbstractExampleUpdaterPostgres implements ExampleUpdater {
  protected abstract Nf36Updater createUpdater();

  @Override
  public ChairUpdate chair() {
    return new ChairUpdateImpl(createUpdater());
  }

  @Override
  public CharmUpdate charm() {
    return new CharmUpdateImpl(createUpdater());
  }

  @Override
  public ClientUpdate client() {
    return new ClientUpdateImpl(createUpdater());
  }

  @Override
  public ClientAddressUpdate clientAddress() {
    return new ClientAddressUpdateImpl(createUpdater());
  }

  @Override
  public PersonUpdate person() {
    return new PersonUpdateImpl(createUpdater());
  }

  @Override
  public StoneUpdate stone() {
    return new StoneUpdateImpl(createUpdater());
  }

  @Override
  public StreetUpdate street() {
    return new StreetUpdateImpl(createUpdater());
  }

  @Override
  public WowUpdate wow() {
    return new WowUpdateImpl(createUpdater());
  }

}
