package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.structure.Client;
import kz.greetgo.db.nf36.structure.ClientAddress;
import kz.greetgo.db.nf36.structure.Street;
import org.testng.annotations.Test;

import java.util.List;

public class JavaGeneratorTest {
  @Test
  public void testName() throws Exception {
    List<Nf3Table> nf3TableList = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .collect();

    JavaGenerator.newGenerator()
      .setOutDir("greetgo.nf36.gen/build/java_generated")
      .setInterfaceBasePackage("kz.greetgo.db.nf36.test.generated.faces")
      .setImplBasePackage("kz.greetgo.db.nf36.test.generated.impl")
      .setSourceBasePackage(Client.class.getPackage().getName())
      .generate(nf3TableList)
    ;
  }

}