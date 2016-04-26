package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.parser.StruShaper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;

public class Nf6GeneratorTest {

  @DataProvider
  public Object[][] dataProvider() {
    return new Object[][]{

        new Object[]{true, null},

        new Object[]{true, UserIdFieldType.Long},

        new Object[]{true, UserIdFieldType.Str},

        new Object[]{false, UserIdFieldType.Str},

    };
  }

  @Test(dataProvider = "dataProvider")
  public void postgres(boolean useNf6, UserIdFieldType userIdFieldType) throws Exception {
    String buildDir = buildDir();

    URL url = getClass().getResource("example2.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);

    new File(buildDir).mkdirs();

    Conf conf = new Conf();
    conf.genOperTables = true;
    conf.userIdFieldType = userIdFieldType;
    conf.useNf6 = useNf6;

    Nf6Generator nf6generator = new Nf6GeneratorPostgres(conf, sg);
    nf6generator.libType = LibType.GBATIS;
    String fp = uiftToPrefix(userIdFieldType);
    PrintStream out = new PrintStream(buildDir + "/ddl-" + fp + "postgres-nf6.sql", "UTF-8");
    PrintStream outP = new PrintStream(buildDir + "/ddl-" + fp + "postgres-nf6-programs.sql", "UTF-8");
    PrintStream outComment = new PrintStream(buildDir + "/ddl-" + fp + "postgres-nf6-comment.sql",
        "UTF-8");
    nf6generator.printSqls(out);
    nf6generator.printPrograms(outP);
    nf6generator.printComment(outComment);
    out.close();
    outP.close();
    outComment.close();

    String srcOutDir = srcOutDir();

    nf6generator.conf.javaGenDir = srcOutDir + "/gen_src";
    nf6generator.conf.modelPackage = "kz.pompei.db_model";
    nf6generator.conf.daoPackage = "kz.pompei.dao";

    nf6generator.conf.javaGenStruDir = srcOutDir + "/gen_src_struct";
    nf6generator.conf.modelStruPackage = "kz.pompei.db_model_stru";
    nf6generator.conf.modelStruExtends = "kz.greetgo.gbatis.util.ModelParent";
    //nf6generator.conf.modelStruImplements = "kz.greetgo.sql_manager.gen.ModelParent";

    nf6generator.generateJavaCodeForPostgres = true;
    nf6generator.generateJavaCodeForOracle = false;
    if (!useNf6) {
      nf6generator.generateDao = false;
      nf6generator.generateVT = false;
      nf6generator.generateChanges = true;
      nf6generator.changeImplementInfo = "kz.greetgo.gbatis.util.HasIdForTests;getId_forTests";
    }
    nf6generator.generateJava();
  }

  @Test
  public void postgres_noNf6() throws Exception {
    postgres(false, UserIdFieldType.Str);
  }

  private static final String GBATIS_MODULE_NAME = "greetgo.gbatis";

  private String srcOutDir() {
    if (new File(GBATIS_MODULE_NAME).isDirectory()) return GBATIS_MODULE_NAME + "/build";
    if (new File("../" + GBATIS_MODULE_NAME).isDirectory()) return "../" + GBATIS_MODULE_NAME + "/build";
    throw new RuntimeException("Cannot find folder greetgo.gbatis");
  }

  private static final String THIS_MODULE_NAME = "greetgo.sqlmanager";

  private String buildDir() {
    String prefix = "";
    if (new File(THIS_MODULE_NAME).isDirectory()) prefix = THIS_MODULE_NAME + "/";
    return prefix + "build";
  }

  private String uiftToPrefix(UserIdFieldType uift) {
    if (uift == null) return "";
    return "modi-" + uift.name() + "-";
  }

  @Test(dataProvider = "dataProvider")
  public void oracle(UserIdFieldType userIdFieldType) throws Exception {
    String buildDir = buildDir();

    URL url = getClass().getResource("example2.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);

    new File(buildDir).mkdirs();

    Conf conf = new Conf();
    conf.genOperTables = true;
    conf.oracleInsertDupValues = true;
    conf.userIdFieldType = userIdFieldType;

    Nf6Generator nf6generator = new Nf6GeneratorOracle(conf, sg);
    nf6generator.libType = LibType.GBATIS;
    String fp = uiftToPrefix(userIdFieldType);
    PrintStream out = new PrintStream(buildDir + "/ddl-" + fp + "oracle-nf6.sql", "UTF-8");
    PrintStream outCopy = new PrintStream(buildDir + "/ddl-" + fp + "oracle-copy.sql", "UTF-8");
    PrintStream outP = new PrintStream(buildDir + "/ddl-" + fp + "oracle-nf6-programs.sql", "UTF-8");
    nf6generator.printSqls(out);
    nf6generator.printPrograms(outP);
    nf6generator.printHistToOperSqls(outCopy, ";;-- NAME --");
    out.close();
    outP.close();
    outCopy.close();

    String srcOutDir = srcOutDir();

    nf6generator.conf.javaGenDir = srcOutDir + "/gen_src";
    nf6generator.conf.modelPackage = "kz.pompei.db_model";
    nf6generator.conf.daoPackage = "kz.pompei.dao";

    nf6generator.conf.javaGenStruDir = srcOutDir + "/gen_src_struct";
    nf6generator.conf.modelStruPackage = "kz.pompei.db_model_stru";
    nf6generator.conf.modelStruExtends = "kz.greetgo.gbatis.util.ModelParent";
    //nf6generator.conf.modelStruImplements = "kz.greetgo.sqlmanager.gen.ModelParent";

    nf6generator.generateJavaCodeForPostgres = false;
    nf6generator.generateJavaCodeForOracle = true;
    nf6generator.generateJava();
  }
}
