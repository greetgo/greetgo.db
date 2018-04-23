package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.nf36.gen.example.beans.BeanConfigAll;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleNf3Door;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import org.testng.annotations.Test;

@ContainerConfig(BeanConfigAll.class)
public class ExampleNf3DoorTest extends AbstractDepinjectTestNg {

  public BeanGetter<ExampleNf3Door> exampleNf3Door;

  @Test
  public void upsertClient() throws Exception {
    exampleNf3Door.get().upsertClient(234).name("asd").go();
  }
}
