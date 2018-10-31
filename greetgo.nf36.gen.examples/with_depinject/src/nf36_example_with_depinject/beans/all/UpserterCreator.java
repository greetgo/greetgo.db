package nf36_example_with_depinject.beans.all;

import kz.greetgo.db.nf36.core.Nf36Upserter;

public interface UpserterCreator {
  Nf36Upserter create();
}
