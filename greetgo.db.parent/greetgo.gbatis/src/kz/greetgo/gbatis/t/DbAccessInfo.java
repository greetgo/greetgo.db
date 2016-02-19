package kz.greetgo.gbatis.t;

import kz.greetgo.db.Jdbc;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;

public interface DbAccessInfo {
  Conf conf();

  Stru stru();

  Jdbc jdbc();
}
