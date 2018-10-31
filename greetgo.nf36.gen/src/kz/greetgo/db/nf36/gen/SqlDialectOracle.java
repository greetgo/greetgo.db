package kz.greetgo.db.nf36.gen;

public class SqlDialectOracle extends SqlDialectAbstract {
  @Override
  protected String strType() {
    return "VARCHAR2";
  }

  @Override
  protected String bigintType() {
    return "NUMBER";
  }

  @Override
  protected String smallintType() {
    return "INT";
  }

  @Override
  protected String intType(int length) {
    switch (length) {
      case 4:
        return smallintType();
      case 8:
        return bigintType();
      default:
        throw new IllegalArgumentException("No integer with length = " + length);
    }
  }

  @Override
  protected String clobType() {
    return "NCLOB";
  }

  @Override
  protected String now() {
    return "systimestamp";
  }

}