package kz.greetgo.gbatis.gen;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import kz.greetgo.gbatis.gen.autoimpl_for_tests.TestDao6;
import kz.greetgo.java_compiler.FilesClassLoader;
import kz.greetgo.java_compiler.JavaCompiler;
import kz.greetgo.java_compiler.JavaCompilerFactory;
import org.testng.annotations.Test;

public class AutoImplementorTest {

  @Test
  public void generate() throws Exception {
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
    //String srcDir = "build/autoimpl_" + sdf.format(new Date());
    String srcDir = "build/autoimpl_asd";

    final TestDbAccessInfo tda = new TestDbAccessInfo();
    AutoImplementor ai = new AutoImplementor(srcDir, tda);

    final GenResult gr = ai.generate(TestDao6.class);

    String fileName = gr.javaFile().toString();
    final File classFile = new File(fileName.substring(0, fileName.lastIndexOf('.')) + ".class");
    boolean needCompile = true;

    if (classFile.exists() && gr.hashFile().exists() && gr.hashFile().lastModified() <= classFile.lastModified()) {
      needCompile = false;
    }

    if (needCompile) {
      System.out.println("--- COMPILING " + gr.javaFile());
      final JavaCompiler compiler = JavaCompilerFactory.createDefault();
      compiler.compile(gr.javaFile());
    }

    FilesClassLoader filesClassLoader = new FilesClassLoader(getClass().getClassLoader());
    filesClassLoader.addClasspath(new File(srcDir));

    final Class<?> implClass = filesClassLoader.loadClass(gr.implClassName());

    final TestDao6 testDao6impl = (TestDao6) implClass.newInstance();

    assertThat(testDao6impl).isNotNull();
  }
}