package kz.greetgo.gbatis2.gen_sql.ddl;

public class DefaultCurrentTimestamp implements FieldDefault {

  public final static DefaultCurrentTimestamp INSTANCE = new DefaultCurrentTimestamp();

  private DefaultCurrentTimestamp() {
  }

  @Override
  public String toString() {
    return "current_timestamp";
  }

  @Override
  public <T> T visitPart(DdlVisitor<T> visitor) {
    return visitor.visitDefaultCurrentTimestamp(this);
  }
}
