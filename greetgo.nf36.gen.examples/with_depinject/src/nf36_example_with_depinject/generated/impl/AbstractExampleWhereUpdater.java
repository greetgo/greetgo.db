package nf36_example_with_depinject.generated.impl;

import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import nf36_example_with_depinject.generated.faces.ExampleWhereUpdater;
import nf36_example_with_depinject.generated.faces.update_where.ClientUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.PersonUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.StreetUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.ChairUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.CharmUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.ClientAddressUpdateWhere;
import nf36_example_with_depinject.generated.faces.update_where.inner.WowUpdateWhere;
import nf36_example_with_depinject.generated.impl.update_where.ClientUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.PersonUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.StreetUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.inner.ChairUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.inner.CharmUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.inner.ClientAddressUpdateWhereImpl;
import nf36_example_with_depinject.generated.impl.update_where.inner.WowUpdateWhereImpl;

public abstract class AbstractExampleWhereUpdater implements ExampleWhereUpdater {
  protected abstract Nf36WhereUpdater createWhereUpdater();

  @Override
  public ChairUpdateWhere chair() {
    return new ChairUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public CharmUpdateWhere charm() {
    return new CharmUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public ClientUpdateWhere client() {
    return new ClientUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public ClientAddressUpdateWhere clientAddress() {
    return new ClientAddressUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public PersonUpdateWhere person() {
    return new PersonUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public StreetUpdateWhere street() {
    return new StreetUpdateWhereImpl(createWhereUpdater());
  }

  @Override
  public WowUpdateWhere wow() {
    return new WowUpdateWhereImpl(createWhereUpdater());
  }

}
