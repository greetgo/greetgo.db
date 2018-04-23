package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.nf36.gen.example.beans.BeanConfigAll;
import kz.greetgo.db.nf36.gen.example.generated.faces.TestNf3Door;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import org.testng.annotations.Test;

@ContainerConfig(BeanConfigAll.class)
public class ConnectTest extends AbstractDepinjectTestNg {

  public BeanGetter<TestNf3Door> testNf3Door;

  @Test
  public void testName() throws Exception {
    testNf3Door.get().upsertClient(234).name("asd").go();
  }
}
