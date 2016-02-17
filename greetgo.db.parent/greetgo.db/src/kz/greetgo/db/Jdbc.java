package kz.greetgo.db;

public interface Jdbc {
  <T> T executeConnection(ConnectionCallback<T> connectionCallback);
}
