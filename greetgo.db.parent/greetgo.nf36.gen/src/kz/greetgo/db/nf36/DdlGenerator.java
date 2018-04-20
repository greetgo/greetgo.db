package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DdlGenerator {
  private Map<Class<?>, Nf3Table> nf3TableMap;

  private DdlGenerator() {}

  public static DdlGenerator newGenerator() {
    return new DdlGenerator();
  }

  public DdlGenerator setNf3TableList(List<Nf3Table> nf3TableList) {
    nf3TableMap = nf3TableList.stream().collect(Collectors.toMap(Nf3Table::source, t -> t));
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
    for (Nf3Table nf3Table : nf3TableMap.values()) {
      printCreateTableFor(nf3Table, out);
    }
  }

  private void printCreateTableFor(Nf3Table nf3Table, PrintStream out) {
    out.println("create table " + nf3Table.nf3prefix() + nf3Table.tableName() + " (");
    for (Nf3Field field : nf3Table.fields()) {
      printCreateField(field, out);
    }
    out.println(");;");
  }

  private void printCreateField(Nf3Field field, PrintStream out) {

  }
}
