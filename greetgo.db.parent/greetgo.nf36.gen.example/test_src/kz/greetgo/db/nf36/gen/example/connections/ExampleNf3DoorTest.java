package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.gen.example.beans.BeanConfigAll;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleNf3Door;
import kz.greetgo.db.nf36.gen.example.jdbc.ByOne;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigAll.class)
public class ExampleNf3DoorTest extends AbstractDepinjectTestNg {

  public BeanGetter<ExampleNf3Door> exampleNf3Door;

  public BeanGetter<Jdbc> jdbc;

  @Test
  public void upsertClient() throws Exception {

    long id = RND.plusLong(1_000_000_000_000L);
    String expectedName1 = "name1 " + RND.str(10);


    exampleNf3Door.get().upsertClient(id)
      .name(expectedName1)
      .go();

    {
      String actualName = jdbc.get().execute(new ByOne<String>("id", id, "client", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", id, "client", "surname"));
      assertThat(actualSurname).isNull();
    }

    String expectedName2 = "name2 " + RND.str(10);

    exampleNf3Door.get().upsertClient(id)
      .name(expectedName2)
      .go();

    {
      String actualName = jdbc.get().execute(new ByOne<String>("id", id, "client", "name"));
      assertThat(actualName).isEqualTo(expectedName2);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", id, "client", "surname"));
      assertThat(actualSurname).isNull();
    }

    String expectedSurname = "surname " + RND.str(10);
    String expectedName3 = "name2 " + RND.str(10);

    exampleNf3Door.get().upsertClient(id)
      .name(expectedName3)
      .surname(expectedSurname)
      .go();

    {
      String actualName = jdbc.get().execute(new ByOne<String>("id", id, "client", "name"));
      assertThat(actualName).isEqualTo(expectedName3);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", id, "client", "surname"));
      assertThat(actualSurname).isEqualTo(expectedSurname);
    }
  }
}
