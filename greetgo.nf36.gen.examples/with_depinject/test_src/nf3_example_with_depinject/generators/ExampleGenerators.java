package nf3_example_with_depinject.generators;

import kz.greetgo.db.nf36.gen.*;
import kz.greetgo.db.worker.db.DbParameters;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import nf36_example_with_depinject.structure.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static kz.greetgo.db.worker.util.Places.withDepinjectDir;

@Bean
public class ExampleGenerators {

  public BeanGetter<DbParameters> dbParameters;

  public BeanGetter<SqlDialect> sqlDialect;

  private final Logger logger = Logger.getLogger(getClass().toString());

  private JavaGenerator javaGenerator;

  private ModelCollector collector;

  private DdlGenerator ddlGenerator;

  public void run() {
    collector = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf3CreatedAtField("created_at")
      .setNf3ModifiedAtField("modified_at")
      .setAuthorFields("created_by", "modified_by", "inserted_by", AuthorType.STR, 32)
      .setMoreMethodName("more")
      .setCommitMethodName("commit")
      .setSequencePrefix("s_")
      .setIdLength(32)
      .setLongLength(2000)
      .setShortLength(50)
      .setDefaultLength(300)
      .setEnumLength(50)
      .setNf6Enabled(false)
      .scanPackageOfClassRecursively(Client.class, true);

    ddlGenerator = DdlGenerator.newGenerator(collector)
      .setSqlDialect(new SqlDialectPostgres())
      .setCommandSeparator(";;");
  }

  public void generateJava() {
    run();

    javaGenerator = JavaGenerator.newGenerator(collector)
      .setOutDir(withDepinjectDir() + "/src")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("nf36_example_with_depinject.generated.faces")
      .setImplBasePackage("nf36_example_with_depinject.generated.impl." + dbParameters.get().baseSubPackage())
      .setUpdaterClassName("DbUpdater")
      .setUpserterClassName("DbUpserter")
      .setGenerateSaver(true)
      .setAbstracting(true);

    javaGenerator.generate();
  }


  List<String> generateSqlFilesAndGetText() throws Exception {
    run();
    String dir = withDepinjectDir() + "/build/gen_sql/" + sqlDialect.getClass().getSimpleName() + "/";
    List<File> sqlFileList = new ArrayList<>();
    {
      File outFile = new File(dir + "/build/gen_sql/001_nf3_tables.sql");
      ddlGenerator.generateNf3Tables(outFile);
      sqlFileList.add(outFile);
      logger.info("Сгенерирован SQL file: " + outFile.getPath());
    }

    {
      File outFile = new File(dir + "/build/gen_sql/003_nf3_references.sql");
      ddlGenerator.generateNf3References(outFile);
      sqlFileList.add(outFile);
      logger.info("Сгенерирован SQL file: " + outFile.getPath());
    }

    {
      StringBuilder sb = new StringBuilder();
      sb.append("create schema m;;\n");
      for (File file : sqlFileList) {
        appendToSB0(new FileInputStream(file), sb);
        sb.append(";;");
      }
      return Arrays.stream(sb.toString().split(";;"))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
    }
  }

  public static void copyStreamsAndCloseIn(InputStream in, OutputStream out) {
    try {
      byte buffer[] = new byte[4096];

      while (true) {
        int read = in.read(buffer);
        if (read < 0) {
          break;
        }
        out.write(buffer, 0, read);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        //noinspection ThrowFromFinallyBlock
        throw new RuntimeException(e);
      }
    }
  }


  private static void appendToSB0(InputStream in, StringBuilder sb) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    copyStreamsAndCloseIn(in, bout);
    sb.append(new String(bout.toByteArray(), "UTF-8"));
  }

}
