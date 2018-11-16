package kz.greetgo.db.worker.connector;

import kz.greetgo.db.DbType;
import kz.greetgo.db.Jdbc;

public interface DatabaseConnector {
  void prepareDatabase() throws Exception;

  DbType type();

  Jdbc jdbc();
}
