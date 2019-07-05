package kz.greetgo.gbatis.gen;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import kz.greetgo.java_compiler.FilesClassLoader;
import kz.greetgo.java_compiler.JavaCompiler;
import kz.greetgo.java_compiler.JavaCompilerFactory;
import kz.greetgo.util.ServerUtil;
import org.testng.annotations.Test;

public class PackageAutoImplementorTest {
  @Test
  public void autoImplementPackage() throws Exception {

    String srcDir = "build/gen_src_package";

    String packageName = "kz.greetgo.gbatis.gen.autoimpl_for_tests";

    final TestDbAccessInfo tda = new TestDbAccessInfo();
    AutoImplementor ai = new AutoImplementor(srcDir, tda);

    final List<GenResult> genResults = PackageAutoImplementor.autoImplementPackage(ai, packageName);

    assertThat(genResults.size()).isGreaterThanOrEqualTo(2);

    FilesClassLoader filesClassLoader = new FilesClassLoader(getClass().getClassLoader());
    filesClassLoader.addClasspath(new File(srcDir));

    final Class<?> aClass = filesClassLoader.loadClass(genResults.get(0).implClassName());

    final Object daoInstance = aClass.newInstance();

    assertThat(daoInstance).isNotNull();
  }
}