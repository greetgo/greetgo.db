package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DdlGenerator {
  private Map<Class<?>, Nf3Table> nf3TableMap;
  private SqlDialect sqlDialect;
  private String commandSeparator = ";;";

  private DdlGenerator() {}

  public static DdlGenerator newGenerator() {
    return new DdlGenerator();
  }

  public DdlGenerator setSqlDialect(SqlDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
    return this;
  }

  public DdlGenerator setNf3TableList(List<Nf3Table> nf3TableList) {
    nf3TableMap = nf3TableList.stream().collect(Collectors.toMap(Nf3Table::source, t -> t));
    return this;
  }

  public DdlGenerator setCommandSeparator(String commandSeparator) {
    this.commandSeparator = commandSeparator;
    return this;
  }

  public DdlGenerator generateCreateTables(File createTablesFile) {

    createTablesFile.getParentFile().mkdirs();

    try (PrintStream pr = new PrintStream(createTablesFile, "UTF-8")) {

      generateCreateTablesTo(pr);

    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  private void generateCreateTablesTo(PrintStream out) {

    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printCreateTableFor(nf3Table, out));

  }

  private void printCreateTableFor(Nf3Table nf3Table, PrintStream out) {
    sqlDialect.checkObjectName(nf3Table.tableName(), ObjectNameType.TABLE_NAME);

    out.println("create table " + nf3Table.nf3prefix() + nf3Table.tableName() + " (");

    for (Nf3Field field : nf3Table.fields()) {
      printCreateField(field, out);
    }

    checkIdOrdering(nf3Table.source(), nf3Table.fields().stream()
      .filter(Nf3Field::isId)
      .mapToInt(Nf3Field::idOrder)
      .sorted()
      .toArray());

    out.println("  primary key(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::dbName)
        .collect(Collectors.joining(", "))

    ) + ")");

    out.println(")" + commandSeparator);
  }

  private void checkIdOrdering(Class<?> source, int[] idArray) {
    for (int i = 1; i <= idArray.length; i++) {
      if (i != idArray[i - 1]) throw new RuntimeException("Incorrect id ordering in " + source);
    }
  }

  private void printCreateField(Nf3Field field, PrintStream out) {
    sqlDialect.checkObjectName(field.dbName(), ObjectNameType.TABLE_FIELD_NAME);
    String fieldDefinition = sqlDialect.createFieldDefinition(field.dbType(), field.dbName());
    out.println("  " + fieldDefinition + ",");
  }
}
