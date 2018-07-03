package nf36_example_with_depinject.generators;

import kz.greetgo.db.nf36.gen.AuthorType;
import kz.greetgo.db.nf36.gen.DdlGenerator;
import kz.greetgo.db.nf36.gen.JavaGenerator;
import kz.greetgo.db.nf36.gen.ModelCollector;
import kz.greetgo.db.nf36.gen.SqlDialect;
import kz.greetgo.db.worker.DbParameters;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.core.HasAfterInject;
import nf36_example_with_depinject.structure.Client;
import nf36_example_with_depinject.structure.ManyIds;
import nf36_example_with_depinject.structure.Person;
import nf36_example_with_depinject.structure.Stone;
import nf36_example_with_depinject.structure.Street;
import nf36_example_with_depinject.structure.inner.Chair;
import nf36_example_with_depinject.structure.inner.Charm;
import nf36_example_with_depinject.structure.inner.ClientAddress;
import nf36_example_with_depinject.structure.inner.Wow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.db.worker.Places.withDepinjectDir;

@Bean
public class ExampleGenerators implements HasAfterInject {

  public BeanGetter<SqlDialect> sqlDialect;

  public BeanGetter<DbParameters> dbParameters;

  private JavaGenerator javaGenerator;

  @Override
  public void afterInject() throws Exception {
    ModelCollector collector = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix(dbParameters.get().nf6prefix())
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
      .setSequencePrefix("s_")
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .register(new Wow())
      .register(new Person())
      .register(new Stone())
      .register(new ManyIds())
      .register(new Charm());

    javaGenerator = JavaGenerator.newGenerator(collector)
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir(withDepinjectDir() + "/src")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("nf36_example_with_depinject.generated.faces")
      .setImplBasePackage("nf36_example_with_depinject.generated.impl." + dbParameters.get().baseSubPackage())
      .setSourceBasePackage(Client.class.getPackage().getName())
      .setUpserterClassName("ExampleUpserter")
      .setUpdaterClassName("ExampleUpdater")
      .setUpserterImplClassName("AbstractExampleUpserter" + dbParameters.get().mainClassesSuffix())
      .setUpdaterImplClassName("AbstractExampleUpdater" + dbParameters.get().mainClassesSuffix())
      .setAbstracting(true)
    ;

    ddlGenerator = DdlGenerator.newGenerator(collector)
      .setSqlDialect(sqlDialect.get())
      .setCommandSeparator(";;")
    ;
  }

  private DdlGenerator ddlGenerator;

  public void generateJava() {
    javaGenerator.generate();
  }

  public List<File> generateSqlFiles() {
    String dir = withDepinjectDir() + "/build/gen_sql/" + sqlDialect.getClass().getSimpleName() + "/";
    List<File> sqlFileList = new ArrayList<>();

    {
      File outFile = new File(dir + "001_nf3_tables.sql");
      ddlGenerator.generateNf3Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "002_sequences.sql");
      ddlGenerator.generateSequences(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "003_nf6_tables.sql");
      ddlGenerator.generateNf6Tables(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "004_nf3_references.sql");
      ddlGenerator.generateNf3References(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "005_nf6_id_references.sql");
      ddlGenerator.generateNf6IdReferences(outFile);
      sqlFileList.add(outFile);
    }

    {
      File outFile = new File(dir + "006_comments.sql");
      ddlGenerator.generateComments(outFile);
      sqlFileList.add(outFile);
    }

    return sqlFileList;
  }
}
