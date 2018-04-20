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

  @Test
  public void resolveFullName_1() throws Exception {
    String fullName = UtilsNf36.resolveFullName("asd.dsa", "Hello");
    assertThat(fullName).isEqualTo("asd.dsa.Hello");
  }

  @Test
  public void resolveFullName_2() throws Exception {
    String fullName = UtilsNf36.resolveFullName(null, "Hello");
    assertThat(fullName).isEqualTo("Hello");
  }

  @Test
  public void javaNameToDbName_1() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("firstName");
    assertThat(dbName).isEqualTo("first_name");
  }

  @Test
  public void javaNameToDbName_2() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("hello");
    assertThat(dbName).isEqualTo("hello");
  }

  @Test
  public void javaNameToDbName_3() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("helloDTO");
    assertThat(dbName).isEqualTo("hello_d_t_o");
  }

  @Test
  public void javaNameToDbName_4() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("HelloWorld");
    assertThat(dbName).isEqualTo("_hello_world");
  }

  @Test
  public void javaNameToDbName_5() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("первоеИмя");
    assertThat(dbName).isEqualTo("первое_имя");
  }

  @Test
  public void javaNameToDbName_6() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("привет");
    assertThat(dbName).isEqualTo("привет");
  }

  @Test
  public void javaNameToDbName_7() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("приветСМИ");
    assertThat(dbName).isEqualTo("привет_с_м_и");
  }

  @Test
  public void javaNameToDbName_8() throws Exception {
    String dbName = UtilsNf36.javaNameToDbName("ПриветМир");
    assertThat(dbName).isEqualTo("_привет_мир");
  }
}