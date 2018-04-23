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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DdlGenerator {
  private Map<Class<?>, Nf3Table> nf3TableMap;
  private SqlDialect sqlDialect;
  private String commandSeparator = ";;";
  private String nf6TableSeparator = "_";
  private String nf6timeField = "ts";

  private DdlGenerator() {}

  public static DdlGenerator newGenerator() {
    return new DdlGenerator();
  }

  public DdlGenerator setSqlDialect(SqlDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
    return this;
  }

  @SuppressWarnings("unused")
  public DdlGenerator setNf6timeField(String nf6timeField) {
    this.nf6timeField = nf6timeField;
    return this;
  }

  @SuppressWarnings("unused")
  public DdlGenerator setNf6TableSeparator(String nf6TableSeparator) {
    this.nf6TableSeparator = nf6TableSeparator;
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

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf3Tables(File outFile) {
    return pushInFile(outFile, this::generateNf3TablesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf6Tables(File outFile) {
    return pushInFile(outFile, this::generateNf6TablesTo);
  }


  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf3References(File outFile) {
    return pushInFile(outFile, this::generateNf3ReferencesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf6IdReferences(File outFile) {
    return pushInFile(outFile, this::generateNf6IdReferencesTo);
  }


  private DdlGenerator pushInFile(File file, Consumer<PrintStream> consumer) {
    file.getParentFile().mkdirs();

    try (PrintStream pr = new PrintStream(file, "UTF-8")) {

      consumer.accept(pr);

    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  private void generateNf3TablesTo(PrintStream out) {
    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printCreateNf3TableFor(nf3Table, out));
  }

  private void generateNf6TablesTo(PrintStream out) {
    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printCreateNf6TableFor(nf3Table, out));
  }

  private void printCreateNf3TableFor(Nf3Table nf3Table, PrintStream out) {
    sqlDialect.checkObjectName(nf3Table.nf3TableName(), ObjectNameType.TABLE_NAME);

    out.println("create table " + nf3Table.nf3TableName() + " (");

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

  private void printCreateNf6TableFor(Nf3Table nf3Table, PrintStream out) {
    nf3Table.fields().stream()
      .filter(f -> !f.isId() && !f.isReference())
      .forEachOrdered(field -> printCreateNf6Table(nf3Table, field, out));

    nf3Table.fields().stream()
      .filter(f -> !f.isId() && f.isRootReference())
      .forEachOrdered(field -> printCreateNf6Table(nf3Table, field, out));
  }

  private void printCreateNf6Table(Nf3Table nf3Table, Nf3Field field, PrintStream out) {
    String nf6tableName = getNf6TableName(nf3Table, field);
    sqlDialect.checkObjectName(nf6tableName, ObjectNameType.TABLE_NAME);

    out.println("create table " + nf6tableName + " (");

    nf3Table.fields().stream()
      .filter(Nf3Field::isId)
      .sorted(Comparator.comparing(Nf3Field::idOrder))
      .forEachOrdered(f -> printCreateField(f, out));

    out.println("  " + sqlDialect.fieldTimestampWithDefaultNow(nf6timeField) + ",");

    if (field.isRootReference()) {
      for (Nf3Field f : field.referenceFields()) {
        printCreateField(f, out);
      }
    } else {
      printCreateField(field, out);
    }

    out.println("  primary key(" + nf3Table.commaSeparatedIdDbNames() + ", " + nf6timeField + ")");

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

  private void generateNf3ReferencesTo(PrintStream out) {
    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printNf3ReferenceFor(nf3Table, out));
  }

  private void generateNf6IdReferencesTo(PrintStream out) {
    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printNf6IdReferenceFor(nf3Table, out));
  }


  private void printNf3ReferenceFor(Nf3Table nf3Table, PrintStream out) {

    nf3Table.fields().stream().filter(Nf3Field::isRootReference).forEachOrdered(root ->

      out.println("alter table " + nf3Table.nf3TableName() + " add foreign key (" + (

        root.referenceDbNames().stream().collect(Collectors.joining(", "))

      ) + ") references " + root.referenceTo().nf3TableName() + " (" + (

        root.referenceTo().commaSeparatedIdDbNames()

      ) + ")" + commandSeparator)

    );

  }


  private String getNf6TableName(Nf3Table nf3Table, Nf3Field field) {
    return nf3Table.nf6prefix() + nf3Table.tableName() + nf6TableSeparator + field.dbName();
  }

  private void printNf6IdReferenceFor(Nf3Table nf3Table, PrintStream out) {
    nf3Table.fields().stream()
      .filter(f -> !f.isId() && (!f.isReference() || f.isRootReference()))
      .forEachOrdered(field ->

        out.println("alter table " + getNf6TableName(nf3Table, field) + " add foreign key"
          + " (" + nf3Table.commaSeparatedIdDbNames() + ") references " + nf3Table.nf3TableName()
          + " (" + nf3Table.commaSeparatedIdDbNames() + ")" + commandSeparator
        )

      );
  }

}
