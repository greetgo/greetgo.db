package nf36_example_with_depinject.generators;

import kz.greetgo.db.nf36.gen.AuthorType;
import kz.greetgo.db.nf36.gen.DdlGenerator;
import kz.greetgo.db.nf36.gen.JavaGenerator;
import kz.greetgo.db.nf36.gen.ModelCollector;
import kz.greetgo.db.nf36.gen.SqlDialect;
import kz.greetgo.db.nf36.gen.SqlDialectPostgres;
import nf36_example_with_depinject.structure.Client;
import nf36_example_with_depinject.structure.Person;
import nf36_example_with_depinject.structure.Street;
import nf36_example_with_depinject.structure.inner.Chair;
import nf36_example_with_depinject.structure.inner.Charm;
import nf36_example_with_depinject.structure.inner.ClientAddress;
import nf36_example_with_depinject.structure.inner.Wow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.db.worker.Places.nf36ExampleWithDepinjectDir;

public class ExampleGenerators {

  private JavaGenerator javaGenerator;

  public ExampleGenerators() {
    ModelCollector collector = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .setEnumLength(51)
      .setNf3CreatedAtField("created_at")
      .setNf3ModifiedAtField("mod_at")
      .setAuthorFields("created_by", "modified_by", "inserted_by", AuthorType.STR, 37)
      .setIdLength(31)
      .setDefaultLength(301)
      .setShortLength(51)
      .setLongLength(2001)
      .setCommitMethodName("commit")
      .setMoreMethodName("more")
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .register(new Wow())
      .register(new Person())
      .register(new Charm());

    javaGenerator = JavaGenerator.newGenerator(collector)
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir(nf36ExampleWithDepinjectDir() + "/src")
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
    exampleGenerators.generateSqlFiles(new SqlDialectPostgres());
  }

  public void generateJava() {
    javaGenerator.generate();
  }

  public List<File> generateSqlFiles(SqlDialect sqlDialect) {
    ddlGenerator.setSqlDialect(sqlDialect);
    String dir = nf36ExampleWithDepinjectDir() + "/build/gen_sql/" + sqlDialect.getClass().getSimpleName() + "/";
    List<File> sqlFileList = new ArrayList<>();
    {
      File outFile = new File(dir + "001_nf3_tables.sql");
      ddlGenerator.generateNf3Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "002_nf6_tables.sql");
      ddlGenerator.generateNf6Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "003_nf3_references.sql");
      ddlGenerator.generateNf3References(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "004_nf6_id_references.sql");
      ddlGenerator.generateNf6IdReferences(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "005_comments.sql");
      ddlGenerator.generateComments(outFile);
      sqlFileList.add(outFile);
    }

    return sqlFileList;
  }

}
