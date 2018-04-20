package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.structure.Client;
import kz.greetgo.db.nf36.structure.Street;
import kz.greetgo.db.nf36.structure.inner.Chair;
import kz.greetgo.db.nf36.structure.inner.ClientAddress;
import org.testng.annotations.Test;

import java.util.List;

public class JavaGeneratorTest {
  @Test
  public void generate() throws Exception {
    List<Nf3Table> nf3TableList = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .collect();

    JavaGenerator.newGenerator()
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir("greetgo.nf36.gen/build/java_generated")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("kz.greetgo.db.nf36.test.generated.faces")
      .setImplBasePackage("kz.greetgo.db.nf36.test.generated.impl")
      .setSourceBasePackage(Client.class.getPackage().getName())
      .setMainNf36ClassName("TestNf3Door")
      .setMainNf36ClassAbstract(true)
      .setNf3TableList(nf3TableList)
      .generate()
    ;
  }

}