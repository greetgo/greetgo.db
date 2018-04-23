package kz.greetgo.db.nf36.gen.example.generators;

import kz.greetgo.db.nf36.DdlGenerator;
import kz.greetgo.db.nf36.JavaGenerator;
import kz.greetgo.db.nf36.ModelCollector;
import kz.greetgo.db.nf36.SqlDialectPostgres;
import kz.greetgo.db.nf36.gen.example.structure.Client;
import kz.greetgo.db.nf36.gen.example.structure.Street;
import kz.greetgo.db.nf36.gen.example.structure.inner.Chair;
import kz.greetgo.db.nf36.gen.example.structure.inner.Charm;
import kz.greetgo.db.nf36.gen.example.structure.inner.ClientAddress;
import kz.greetgo.db.nf36.gen.example.structure.inner.Wow;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.db.nf36.gen.example.util.Places.exampleDir;

public class ExampleGenerators {

  private JavaGenerator javaGenerator;

  public ExampleGenerators() {
    List<Nf3Table> nf3TableList = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .setEnumLength(50)
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .register(new Wow())
      .register(new Charm())
      .collect();

    javaGenerator = JavaGenerator.newGenerator()
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir(exampleDir() + "/src")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("kz.greetgo.db.nf36.gen.example.generated.faces")
      .setImplBasePackage("kz.greetgo.db.nf36.gen.example.generated.impl")
      .setSourceBasePackage(Client.class.getPackage().getName())
      .setMainNf36ClassName("TestNf3Door")
      .setMainNf36ClassAbstract(true)
      .setNf3TableList(nf3TableList);

    ddlGenerator = DdlGenerator.newGenerator()
      .setNf3TableList(nf3TableList)
      .setSqlDialect(new SqlDialectPostgres())
      .setCommandSeparator(";;");
  }

  private DdlGenerator ddlGenerator;

  public static void main(String[] args) {
    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateAll();
  }

  public void generateJava() {
    javaGenerator.generate();
  }

  public final List<File> sqlFileList = new ArrayList<>();

  public List<File> generateSqlFiles() {
    {
      File createTablesFile = new File(exampleDir() + "/build/gen_sql/001_create_nf3_tables.sql");
      ddlGenerator.generateCreateNf3Tables(createTablesFile);
      sqlFileList.add(createTablesFile);
    }

    {
      File referencesFile = new File(exampleDir() + "/build/gen_sql/002_add_table_nf3_references.sql");
      ddlGenerator.generateNf3References(referencesFile);
      sqlFileList.add(referencesFile);
    }

    return sqlFileList;
  }

  public void generateAll() {
    generateJava();
    generateSqlFiles();
  }
}
