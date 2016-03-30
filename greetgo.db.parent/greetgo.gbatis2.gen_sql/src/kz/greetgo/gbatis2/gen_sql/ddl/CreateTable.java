package kz.greetgo.gbatis2.gen_sql.ddl;

import java.util.ArrayList;
import java.util.List;

public class CreateTable implements Ddl {
  public final String name;
  public final List<String> primaryKey = new ArrayList<>();
  public final List<CreateTableField> fields = new ArrayList<>();

  public CreateTable(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "create table " + name + " (\n" + fieldsAndPrimaryKeys() + ")\n";
  }

  private String fieldsAndPrimaryKeys() {
    StringBuilder sb = new StringBuilder();
    for (CreateTableField field : fields) {
      sb.append("  ").append(field).append("\n");
    }
    sb.append("  primary key (");
    for (String str : primaryKey) {
      sb.append(str).append(' ');
    }
    if (primaryKey.size() > 0) sb.setLength(sb.length() - 1);
    sb.append(")\n");
    return sb.toString();
  }

  @Override
  public <T> T visit(DdlVisitor<T> visitor) {
    return visitor.visitCreateTable(this);
  }
}
