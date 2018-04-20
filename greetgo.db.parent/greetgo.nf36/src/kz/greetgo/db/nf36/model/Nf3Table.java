package kz.greetgo.db.nf36.model;

import java.util.List;

public interface Nf3Table {
  Class<?> source();

  String tableName();

  List<Nf3Field> fields();

  String nf3prefix();

  String nf6prefix();
}
