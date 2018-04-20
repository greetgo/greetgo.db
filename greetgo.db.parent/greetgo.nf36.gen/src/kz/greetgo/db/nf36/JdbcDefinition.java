package kz.greetgo.db.nf36;

public interface JdbcDefinition {
  String jdbcClassName();

  String executeMethodName();//default: execute

  String jdbcAccessMethod();//default: __getJdbc__

  String jdbcVar();//default: __jdbc__

  String connectionCallbackClassName();//default: kz.greetgo.db.ConnectionCallback
}
