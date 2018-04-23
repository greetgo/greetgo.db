package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
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

  public DdlGenerator generateCreateNf3Tables(File createTablesFile) {
    return pushInFile(createTablesFile, this::generateCreateTablesTo);
  }

  @SuppressWarnings("UnusedReturnValue")
  public DdlGenerator generateNf3References(File referencesFile) {
    return pushInFile(referencesFile, this::generateReferencesTo);
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

  private void generateReferencesTo(PrintStream out) {
    nf3TableMap.values().stream()
      .sorted(Comparator.comparing(Nf3Table::tableName))
      .forEachOrdered(nf3Table -> printReferenceFor(nf3Table, out));

  }

  private void printReferenceFor(Nf3Table nf3Table, PrintStream out) {

    Set<String> allReferences = nf3Table.fields().stream()
      .filter(Nf3Field::isReference)
      .map(Nf3Field::javaName)
      .collect(Collectors.toSet());

    Set<String> allNextParts = nf3Table.fields().stream()
      .filter(Nf3Field::hasNextPart)
      .map(Nf3Field::nextPart)
      .collect(Collectors.toSet());

    Set<String> roots = new HashSet<>(allReferences);
    roots.removeAll(allNextParts);

    if (roots.isEmpty()) return;

    allNextParts.removeAll(allReferences);

    if (!allNextParts.isEmpty()) {
      throw new RuntimeException("Tattered next parts " + allNextParts + " (no such fields) in "
        + nf3Table.source().getSimpleName());
    }

    List<List<String>> referenceJavaNameListList = roots.stream()
      .sorted()
      .map(UtilsNf36::mutableList)
      .collect(Collectors.toList());

    Map<String, String> nextPartMap = nf3Table.fields().stream()
      .filter(Nf3Field::hasNextPart).
        collect(Collectors.toMap(Nf3Field::javaName, Nf3Field::nextPart));

    while (true) {
      boolean was = false;

      for (List<String> fieldJavaNameList : referenceJavaNameListList) {
        String fieldJavaName = fieldJavaNameList.get(fieldJavaNameList.size() - 1);
        String nextPart = nextPartMap.remove(fieldJavaName);
        if (nextPart != null) {
          was = true;
          fieldJavaNameList.add(nextPart);
        }
      }

      if (!was) break;
    }

    referenceJavaNameListList.forEach(
      referenceJavaNameList -> printOneReference(referenceJavaNameList, nf3Table, out)
    );
  }

  private void printOneReference(List<String> referenceJavaNameList, Nf3Table nf3Table, PrintStream out) {
    Class<?> referenceTo = nf3Table.getByJavaName(referenceJavaNameList.get(0)).referenceTo();

    Nf3Table destination = nf3TableMap.get(referenceTo);
    if (destination == null) {
      throw new RuntimeException("Broken reference: class " + referenceTo.getSimpleName()
        + " is not registered. Error in " + nf3Table.source().getSimpleName()
        + ". You need register " + referenceTo);
    }

    out.println("alter table " + nf3Table.tableName() + " add foreign key (" + (

      referenceJavaNameList.stream()
        .map(nf3Table::getDbNameByJavaName)
        .collect(Collectors.joining(", "))

    ) + ") references " + destination.tableName() + "(" + (

      destination.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::dbName)
        .collect(Collectors.joining(", "))

    ) + ")" + commandSeparator);
  }

}
