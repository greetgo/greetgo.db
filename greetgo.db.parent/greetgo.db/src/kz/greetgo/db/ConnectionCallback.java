package kz.greetgo.db;

import java.sql.Connection;

/**
 * Callback function for DB access
 *
 * @param <T> the returning type
 */
public interface ConnectionCallback<T> {
  /**
   * Calling method when connection to DB accessed and all ready to use access DB logic
   *
   * @param con connection to DB
   * @return returning value
   * @throws Exception you need not use try/cache
   */
  T doInConnection(Connection con) throws Exception;
}
