package kz.greetgo.gbatis2.gen_sql.postgres;

import kz.greetgo.gbatis2.gen_sql.DdlVisitorToSql;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateSequence;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTable;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTableField;
import kz.greetgo.gbatis2.gen_sql.ddl.DefaultCurrentTimestamp;
import kz.greetgo.gbatis2.struct.model.EssenceVisitor;

public class DdlVisitorToSql_postgres implements DdlVisitorToSql {

  private final EssenceVisitor<String> simpleVisitor = new EssenceEssenceToSqlVisitor();

  public String tab = "  ";
  public String newLine = "\n";

  @Override
  public String visitCreateSequence(CreateSequence createSequence) {
    return "create sequence " + createSequence.name;
  }

  @Override
  public String visitCreateTable(CreateTable createTable) {
    StringBuilder sb = new StringBuilder();
    sb.append("create table ").append(createTable.name).append(" (").append(newLine);
    for (CreateTableField field : createTable.fields) {
      sb.append(tab).append(field.visitPart(this)).append(',').append(newLine);
    }
    if (createTable.primaryKey.size() == 0) {
      sb.setLength(sb.length() - 1);
    } else {
      sb.append(tab).append("primary key (");
      for (String partName : createTable.primaryKey) {
        sb.append(partName).append(", ");
      }
      sb.setLength(sb.length() - 2);
      sb.append(')').append(newLine);
    }
    sb.append(')');
    return sb.toString();
  }

  @Override
  public String visitCreateTableField(CreateTableField createTableField) {
    return createTableField.name + ' ' + createTableField.type.visit(simpleVisitor)
        + (createTableField.notNull ? " not null" : "")
        + (createTableField.fieldDefault == null ? "" : " default " + createTableField.fieldDefault.visitPart(this));
  }

  @Override
  public String visitDefaultCurrentTimestamp(DefaultCurrentTimestamp defaultCurrentTimestamp) {
    return "current_timestamp";
  }
}
