package kz.greetgo.db;

public interface Jdbc {
  <T> T execute(ConnectionCallback<T> connectionCallback);
}
