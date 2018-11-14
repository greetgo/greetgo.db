package kz.greetgo.db.worker.db;

public interface DbConfig {
  String url();

  String username();

  String password();

  String dbName();
}
