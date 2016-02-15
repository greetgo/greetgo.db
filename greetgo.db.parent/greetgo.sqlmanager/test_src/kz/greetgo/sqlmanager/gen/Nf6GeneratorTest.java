package kz.greetgo.sqlmanager.gen;

import static kz.greetgo.util.ServerUtil.dummyCheck;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;

import kz.greetgo.sqlmanager.parser.StruShaper;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Nf6GeneratorTest {
  
  @DataProvider
  public Object[][] dataProvider() {
    return new Object[][] {
    
    new Object[] { null },
    
    new Object[] { UserIdFieldType.Long },
    
    new Object[] { UserIdFieldType.Str },
    
    };
  }
  
  @Test(dataProvider = "dataProvider")
  public void postgres(UserIdFieldType uift) throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    
    dummyCheck(new File("build").mkdir());
    
    Conf conf = new Conf();
    conf.genOperTables = true;
    conf.userIdFieldType = uift;
    
    Nf6Generator nf6generator = new Nf6GeneratorPostgres(conf, sg);
    nf6generator.libType = LibType.GBATIS;
    String fp = uiftToPrefix(uift);
    PrintStream out = new PrintStream("build/ddl-" + fp + "postgres-nf6.sql", "UTF-8");
    PrintStream outP = new PrintStream("build/ddl-" + fp + "postgres-nf6-programs.sql", "UTF-8");
    PrintStream outComment = new PrintStream("build/ddl-" + fp + "postgres-nf6-comment.sql",
        "UTF-8");
    PrintStream outCopy = new PrintStream("build/ddl-" + fp + "oracle-copy.sql", "UTF-8");
    nf6generator.printSqls(out);
    nf6generator.printPrograms(outP);
    nf6generator.printHistToOperSqls(outCopy, ";; -- NAME -- ");
    nf6generator.printComment(outComment);
    out.close();
    outP.close();
    outComment.close();
    outCopy.close();
    
    nf6generator.conf.javaGenDir = "gensrc";
    nf6generator.conf.modelPackage = "kz.pompei.dbmodel";
    nf6generator.conf.daoPackage = "kz.pompei.dao";
    
    nf6generator.conf.javaGenStruDir = "gensrcstru";
    nf6generator.conf.modelStruPackage = "kz.pompei.dbmodelstru";
    nf6generator.conf.modelStruExtends = "kz.greetgo.sqlmanager.gen.ModelParent";
    //nf6generator.conf.modelStruImplements = "kz.greetgo.sqlmanager.gen.ModelParent";
    
    nf6generator.generateJava();
  }
  
  private String uiftToPrefix(UserIdFieldType uift) {
    if (uift == null) return "";
    return "modi-" + uift.name() + "-";
  }
  
  @Test(dataProvider = "dataProvider")
  public void oracle(UserIdFieldType uift) throws Exception {
    URL url = getClass().getResource("example.nf3");
    StruShaper sg = new StruShaper();
    sg.printPStru = false;
    sg.parse(url);
    
    dummyCheck(new File("build").mkdir());
    
    Conf conf = new Conf();
    conf.genOperTables = true;
    conf.oracleInsertDupValues = true;
    conf.userIdFieldType = uift;
    
    Nf6Generator nf6generator = new Nf6GeneratorOracle(conf, sg);
    String fp = uiftToPrefix(uift);
    PrintStream out = new PrintStream("build/ddl-" + fp + "oracle-nf6.sql", "UTF-8");
    PrintStream outCopy = new PrintStream("build/ddl-" + fp + "oracle-copy.sql", "UTF-8");
    PrintStream outP = new PrintStream("build/ddl-" + fp + "oracle-nf6-programs.sql", "UTF-8");
    nf6generator.printSqls(out);
    nf6generator.printPrograms(outP);
    nf6generator.printHistToOperSqls(outCopy, ";;-- NAME --");
    out.close();
    outP.close();
    outCopy.close();
    
    nf6generator.conf.javaGenDir = "gensrc";
    nf6generator.conf.modelPackage = "kz.pompei.dbmodel";
    nf6generator.conf.daoPackage = "kz.pompei.dao";
    
    nf6generator.conf.javaGenStruDir = "gensrcstru";
    nf6generator.conf.modelStruPackage = "kz.pompei.dbmodelstru";
    nf6generator.conf.modelStruExtends = "kz.greetgo.sqlmanager.gen.ModelParent";
    //nf6generator.conf.modelStruImplements = "kz.greetgo.sqlmanager.gen.ModelParent";
    
    //nf6generator.generateJava();
  }
}
