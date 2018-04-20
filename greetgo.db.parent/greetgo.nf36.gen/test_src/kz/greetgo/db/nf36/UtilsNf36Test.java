package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.errors.IllegalPackage;
import org.testng.annotations.Test;

import java.io.File;

import static org.fest.assertions.api.Assertions.assertThat;

public class UtilsNf36Test {

  @Test
  public void calcSubPackage_1() throws Exception {
    String subPackage = UtilsNf36.calcSubPackage("kz.greetgo.wow", "kz.greetgo.wow.asd.dsa");
    assertThat(subPackage).isEqualTo("asd.dsa");
  }

  @Test
  public void calcSubPackage_2() throws Exception {
    String subPackage = UtilsNf36.calcSubPackage("kz.greetgo.wow", "kz.greetgo.wow");
    assertThat(subPackage).isNull();
  }

  @Test(expectedExceptions = IllegalPackage.class)
  public void calcSubPackage_3() throws Exception {
    UtilsNf36.calcSubPackage("kz.greetgo.wow", "kz.greetgo.left.wow");
  }

  @Test
  public void resolvePackage_1() throws Exception {
    String PackageName = UtilsNf36.resolvePackage("kz.greetgo.wow", "asd.dsa");
    assertThat(PackageName).isEqualTo("kz.greetgo.wow.asd.dsa");
  }

  @Test
  public void resolvePackage_2() throws Exception {
    String PackageName = UtilsNf36.resolvePackage("kz.greetgo.wow", null);
    assertThat(PackageName).isEqualTo("kz.greetgo.wow");
  }

  @Test
  public void resolveJavaFile() throws Exception {
    File file = UtilsNf36.resolveJavaFile("build/asd", "kz.greetgo.wow", "Client");
    assertThat(file.getPath()).isEqualTo("build/asd/kz/greetgo/wow/Client.java");
  }

  @Test
  public void extractSimpleName_1() throws Exception {
    String name = UtilsNf36.extractSimpleName("asd.dsa.HelloWorld");
    assertThat(name).isEqualTo("HelloWorld");
  }

  @Test
  public void extractSimpleName_2() throws Exception {
    String name = UtilsNf36.extractSimpleName("HelloWorld");
    assertThat(name).isEqualTo("HelloWorld");
  }
}