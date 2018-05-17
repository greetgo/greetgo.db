package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.adapters.UpserterAdapterBuilder;
import kz.greetgo.db.nf36.adapters.WhereUpdaterBuilder;

public class Nf36Builder {

  private Nf36Builder() {}

  public static Nf36Builder newNf36Builder() {
    return new Nf36Builder();
  }

  public UpserterAdapterBuilder upserter() {
    return new UpserterAdapterBuilder();
  }

  public WhereUpdaterBuilder whereUpdater() {
    return new WhereUpdaterBuilder();
  }
}
