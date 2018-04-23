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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.db.nf36.gen.example.util.Places.exampleDir;

public class ExampleGenerators {

  private JavaGenerator javaGenerator;

  public ExampleGenerators() {
    ModelCollector collector = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .setEnumLength(50)
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .register(new Wow())
      .register(new Charm());

    javaGenerator = JavaGenerator.newGenerator(collector)
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir(exampleDir() + "/src")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("kz.greetgo.db.nf36.gen.example.generated.faces")
      .setImplBasePackage("kz.greetgo.db.nf36.gen.example.generated.impl")
      .setSourceBasePackage(Client.class.getPackage().getName())
      .setMainNf36ClassName("ExampleUpserter")
      .setMainNf36ClassAbstract(true)
    ;

    ddlGenerator = DdlGenerator.newGenerator(collector)
      .setSqlDialect(new SqlDialectPostgres())
      .setCommandSeparator(";;")
    ;
  }

  private DdlGenerator ddlGenerator;

  public static void main(String[] args) {
    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateJava();
    exampleGenerators.generateSqlFiles();
  }

  public void generateJava() {
    javaGenerator.generate();
  }

  public List<File> generateSqlFiles() {
    List<File> sqlFileList = new ArrayList<>();
    {
      File outFile = new File(exampleDir() + "/build/gen_sql/001_nf3_tables.sql");
      ddlGenerator.generateNf3Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(exampleDir() + "/build/gen_sql/002_nf6_tables.sql");
      ddlGenerator.generateNf6Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(exampleDir() + "/build/gen_sql/003_nf3_references.sql");
      ddlGenerator.generateNf3References(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(exampleDir() + "/build/gen_sql/004_nf6_id_references.sql");
      ddlGenerator.generateNf6IdReferences(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(exampleDir() + "/build/gen_sql/005_comments.sql");
      ddlGenerator.generateComments(outFile);
      sqlFileList.add(outFile);
    }

    return sqlFileList;
  }

}
