package kz.greetgo.db.nf36.model;

public interface Nf3Field {

  boolean primaryKey();

  String name();

  DbField db();

  DbTable nf6Table();

  Nf3Table references();
}
