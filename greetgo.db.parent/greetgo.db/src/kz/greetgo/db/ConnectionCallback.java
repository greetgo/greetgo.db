package kz.greetgo.db;

import java.sql.Connection;

public interface ConnectionCallback<T> {
  T doInConnection(Connection connection) throws Exception;
}
