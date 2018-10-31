package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DdlGenerator {
  private final Map<Class<?>, Nf3Table> nf3TableMap;
  private SqlDialect sqlDialect;
  private String commandSeparator = ";;";

  private final ModelCollector collector;

  private DdlGenerator(ModelCollector collector) {
    this.collector = collector;
    nf3TableMap = collector.collect().stream().collect(Collectors.toMap(Nf3Table::source, t -> t));
  }

  public static DdlGenerator newGenerator(ModelCollector modelCollector) {
    return new DdlGenerator(modelCollector);
  }

  public DdlGenerator setSqlDialect(SqlDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
    return this;
  }


  public DdlGenerator setCommandSeparator(String commandSeparator) {
    this.commandSeparator = commandSeparator;
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf3Tables(File outFile) {
    collector.collect();
    return pushInFile(outFile, this::generateNf3TablesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateSequences(File outFile) {
    collector.collect();
    return pushInFile(outFile, this::generateSequencesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateComments(File outFile) {
    collector.collect();
    return pushInFile(outFile, this::generateCommentsTo);
  }


  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf6Tables(File outFile) {
    collector.collect();
    return pushInFile(outFile, this::generateNf6TablesTo);
  }


  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf3References(File outFile) {
    collector.collect();
    return pushInFile(outFile, this::generateNf3ReferencesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf6IdReferences(File outFile) {
    collector.collect();
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

  private void generateSequencesTo(PrintStream out) {
    nf3TableMap.values().stream()
        .flatMap(t -> t.fields().stream())
        .map(Nf3Field::sequence)
        .filter(Objects::nonNull)
        .sorted(Comparator.comparing(s -> s.name))
        .forEachOrdered(s -> out.println(sqlDialect.createSequence(s) + commandSeparator));
  }

  private void generateCommentsTo(PrintStream out) {
    nf3TableMap.values().stream()
        .sorted(Comparator.comparing(Nf3Table::tableName))
        .forEachOrdered(nf3Table -> printCommentsFor(nf3Table, out));
  }

  private void generateNf6TablesTo(PrintStream out) {
    nf3TableMap.values().stream()
        .sorted(Comparator.comparing(Nf3Table::tableName))
        .forEachOrdered(nf3Table -> printCreateNf6TableFor(nf3Table, out));
  }

  private void printCommentsFor(Nf3Table nf3Table, PrintStream out) {
    out.println("comment on table " + nf3Table.nf3TableName()
        + " is '" + nf3Table.commentQuotedForSql() + "'" + commandSeparator);

    for (Nf3Field field : nf3Table.fields()) {
      out.println("comment on column " + nf3Table.nf3TableName() + "." + field.dbName()
          + " is '" + field.commentQuotedForSql() + "'" + commandSeparator);
    }

  }

  private void printCreateNf3TableFor(Nf3Table nf3Table, PrintStream out) {
    sqlDialect.checkObjectName(nf3Table.nf3TableName(), ObjectNameType.TABLE_NAME);

    out.println("create table " + nf3Table.nf3TableName() + " (");

    for (Nf3Field field : nf3Table.fields()) {
      printCreateField(field, out);
    }

    if (collector.nf3CreatedAtField != null) {
      out.println("" + sqlDialect.fieldTimestampWithDefaultNow(collector.nf3CreatedAtField) + ",");
    }
    if (collector.nf3ModifiedAtField != null) {
      out.println("" + sqlDialect.fieldTimestampWithDefaultNow(collector.nf3ModifiedAtField) + ",");
    }

    if (collector.nf3CreatedBy != null) {
      printAuthorField(collector.nf3CreatedBy, out);
    }
    if (collector.nf3ModifiedBy != null) {
      printAuthorField(collector.nf3ModifiedBy, out);
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
    String nf6tableName = collector.getNf6TableName(nf3Table, field);
    sqlDialect.checkObjectName(nf6tableName, ObjectNameType.TABLE_NAME);

    out.println("create table " + nf6tableName + " (");

    nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .forEachOrdered(f -> printCreateField(f, out));

    out.println("  " + sqlDialect.fieldTimestampWithDefaultNow(collector.nf6timeField) + ",");

    if (field.isRootReference()) {
      for (Nf3Field f : field.referenceFields()) {
        printCreateField(f, out);
      }
    } else {
      printCreateField(field, out);
    }

    if (collector.nf6InsertedBy != null) {
      printAuthorField(collector.nf6InsertedBy, out);
    }

    out.println("  primary key(" + nf3Table.commaSeparatedIdDbNames() + ", " + collector.nf6timeField + ")");

    out.println(")" + commandSeparator);
  }


  private void checkIdOrdering(Class<?> source, int[] idArray) {
    for (int i = 1; i <= idArray.length; i++) {
      if (i != idArray[i - 1]) { throw new RuntimeException("Incorrect id ordering in " + source); }
    }
  }

  private void printAuthorField(AuthorField authorField, PrintStream out) {
    sqlDialect.checkObjectName(authorField.name, ObjectNameType.TABLE_FIELD_NAME);

    String fieldDefinition = sqlDialect.createAuthorFieldDefinition(authorField);

    out.println("  " + fieldDefinition + ",");
  }

  private void printCreateField(Nf3Field field, PrintStream out) {
    try {

      sqlDialect.checkObjectName(field.dbName(), ObjectNameType.TABLE_FIELD_NAME);

      String fieldDefinition = sqlDialect.createFieldDefinition(
          field.dbType(), field.dbName(), field.source(), field.definer());

      out.println("  " + fieldDefinition + ",");

    } catch (Exception e) {
      if (e instanceof RuntimeException) { throw (RuntimeException) e; }
      throw new RuntimeException(e);
    }
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

            String.join(", ", root.referenceDbNames())

        ) + ") references " + root.referenceTo().nf3TableName() + " (" + (

            root.referenceTo().commaSeparatedIdDbNames()

        ) + ")" + commandSeparator)

    );

  }

  private void printNf6IdReferenceFor(Nf3Table nf3Table, PrintStream out) {
    nf3Table.fields().stream()
        .filter(f -> !f.isId() && (!f.isReference() || f.isRootReference()))
        .forEachOrdered(field ->

            out.println("alter table " + collector.getNf6TableName(nf3Table, field) + " add foreign key"
                + " (" + nf3Table.commaSeparatedIdDbNames() + ") references " + nf3Table.nf3TableName()
                + " (" + nf3Table.commaSeparatedIdDbNames() + ")" + commandSeparator
            )

        );
  }


}
