package kz.greetgo.gbatis.classscanner;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import kz.greetgo.gbatis.classscanner.ClassScannerDef;

import org.testng.annotations.Test;

public class ClassScannerDefTest {
  @Test
  public void scanPackage() throws Exception {
    ClassScannerDef scanner = new ClassScannerDef();
    Set<Class<?>> classes = scanner.scanPackage("kz.greetgo.gbatis.probes.asd");
    for (Class<?> cl : classes) {
      System.out.println(cl);
    }
    assertThat(1);
  }
}
