package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.adapters.HistorySelectorAdapterBuilder;
import kz.greetgo.db.nf36.adapters.SequenceNextBuilder;
import kz.greetgo.db.nf36.adapters.UpdaterBuilder;
import kz.greetgo.db.nf36.adapters.UpserterAdapterBuilder;
import kz.greetgo.db.nf36.bridges.SaverBuilder;

public class Nf36Builder {

  private Nf36Builder() {}

  public static Nf36Builder newNf36Builder() {
    return new Nf36Builder();
  }

  public UpserterAdapterBuilder upserter() {
    return new UpserterAdapterBuilder();
  }

  public UpdaterBuilder updater() {
    return new UpdaterBuilder();
  }

  public SequenceNextBuilder sequenceNext() {
    return new SequenceNextBuilder();
  }

  public SaverBuilder saver() {
    return new SaverBuilder();
  }

  public HistorySelectorAdapterBuilder historySelector() {
    return new HistorySelectorAdapterBuilder();
  }
}
