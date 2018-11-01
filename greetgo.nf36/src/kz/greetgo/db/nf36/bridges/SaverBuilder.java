package kz.greetgo.db.nf36.bridges;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.SequenceNext;

import java.util.function.Supplier;

public class SaverBuilder {
  private Nf36Upserter upserter = null;

  public SaverBuilder setUpserter(Nf36Upserter upserter) {
    this.upserter = upserter;
    return this;
  }

  private void check() {
    if (built) {
      throw new RuntimeException("Already built. Please create new builder to build new saver");
    }

    if (upserter == null) {
      throw new RuntimeException("upserter == null. Please, define it");
    }
  }

  private boolean built = false;

  public Nf36Saver build() {
    check();
    built = true;
    return new SaverUpserterBridge(upserter);
  }
}
