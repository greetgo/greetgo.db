package kz.greetgo.gbatis2.gen_sql.ddl;

public interface DdlVisitor<T> {
  T visitCreateSequence(CreateSequence createSequence);

  T visitCreateTable(CreateTable createTable);

  T visitCreateTableField(CreateTableField createTableField);

  T visitDefaultCurrentTimestamp(DefaultCurrentTimestamp defaultCurrentTimestamp);
}
