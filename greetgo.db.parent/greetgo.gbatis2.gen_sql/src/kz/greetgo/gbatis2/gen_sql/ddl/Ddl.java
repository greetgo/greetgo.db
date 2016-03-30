package kz.greetgo.gbatis2.gen_sql.ddl;

public interface Ddl {
  <T> T visit(DdlVisitor<T> visitor);
}
