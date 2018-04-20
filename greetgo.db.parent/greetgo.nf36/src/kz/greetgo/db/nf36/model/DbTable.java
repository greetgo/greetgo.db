package kz.greetgo.db.nf36.model;

import java.util.List;

public interface DbTable {
  String name();

  List<DbField> fields();
}
