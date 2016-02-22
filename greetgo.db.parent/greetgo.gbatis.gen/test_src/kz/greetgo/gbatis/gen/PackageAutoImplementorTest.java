package kz.greetgo.gbatis.gen;

import kz.greetgo.util.ServerUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class PackageAutoImplementorTest {
  @Test
  public void autoImplementPackage() throws Exception {

    String srcDir = "build/gen_src_package";

    String packageName = "kz.greetgo.gbatis.gen.autoimpl_for_tests";

    final TestDbAccessInfo tda = new TestDbAccessInfo();
    AutoImplementor ai = new AutoImplementor(srcDir, tda);

    final List<GenResult> genResults = PackageAutoImplementor.autoImplementPackage(ai, packageName);

    assertThat(genResults.size()).isGreaterThanOrEqualTo(2);

    ServerUtil.addToClasspath(new File(srcDir));

    final Class<?> aClass = Class.forName(genResults.get(0).implClassName());

    final Object daoInstance = aClass.newInstance();

    assertThat(daoInstance).isNotNull();
  }
}