package kz.greetgo.db.nf36.gen;

public class SqlDialectPostgres extends SqlDialectAbstract {
  @Override
  protected String strType() {
    return "VARCHAR";
  }

  @Override
  protected String bigintType() {
    return "BIGINT";
  }

  @Override
  protected String smallintType() {
    return "SMALLINT";
  }

  @Override
  protected String intType(int length) {
    return "INT" + length;
  }

  @Override
  protected String clobType() {
    return "TEXT";
  }

  @Override
  protected String now() {
    return "clock_timestamp()";
  }
}
