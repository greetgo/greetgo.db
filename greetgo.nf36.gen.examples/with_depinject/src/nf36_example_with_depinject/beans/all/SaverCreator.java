package nf36_example_with_depinject.beans.all;

import kz.greetgo.db.nf36.core.Nf36Saver;

public interface SaverCreator {
  Nf36Saver create();
}
