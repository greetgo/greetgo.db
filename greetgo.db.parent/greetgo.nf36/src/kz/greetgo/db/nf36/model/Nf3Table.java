package kz.greetgo.db.nf36.model;

import java.util.List;

public interface Nf3Table {
  Class<?> source();

  List<Nf3Field> fields();
}
