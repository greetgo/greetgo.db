package kz.greetgo.gbatis2.gen_sql.ddl;

public interface DdlPart {
  <T> T visitPart(DdlVisitor<T> visitor);
}
