package kz.greetgo.db;

public interface Jdbc {
  <T> T executeConnection(ConnectionExecutor<T> connectionExecutor);
}
