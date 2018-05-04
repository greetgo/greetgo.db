package kz.greetgo.db;

/**
 * Getting access to DB using JDBC protocol.
 */
public interface Jdbc {
  /**
   * DB access evaluating method
   *
   * @param connectionCallback it is realize code to access to DB
   * @param <T>                returning type
   * @return returning value returned by connectionCallback
   */
  <T> T execute(ConnectionCallback<T> connectionCallback);
}
