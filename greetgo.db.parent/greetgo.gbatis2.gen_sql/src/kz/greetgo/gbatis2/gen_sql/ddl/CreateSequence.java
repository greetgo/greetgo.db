package kz.greetgo.gbatis2.gen_sql.ddl;

public class CreateSequence implements Ddl {
  public final String name;

  public CreateSequence(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "create sequence " + name;
  }

  @Override
  public <T> T visit(DdlVisitor<T> visitor) {
    return visitor.visitCreateSequence(this);
  }
}
