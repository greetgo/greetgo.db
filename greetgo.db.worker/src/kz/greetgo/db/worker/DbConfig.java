package kz.greetgo.db.worker;

public interface DbConfig {
  String url();

  String username();

  String password();

  String dbName();
}
