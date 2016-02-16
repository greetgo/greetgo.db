package kz.greetgo.db;

import java.sql.Connection;

public interface ConnectionExecutor<T> {
  T execute(Connection connection) throws Exception;
}
