package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.gen.example.beans.BeanConfigAll;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleUpserter;
import kz.greetgo.db.nf36.gen.example.jdbc.ByOne;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigAll.class)
public class ExampleUpserterTest extends AbstractDepinjectTestNg {

  public BeanGetter<ExampleUpserter> exampleUpserter;

  public BeanGetter<Jdbc> jdbc;

  @Test
  public void upsertClient() throws Exception {

    long id = RND.plusLong(1_000_000_000_000L);
    String expectedName1 = "name1 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commit();

    {
      String actualName = jdbc.get().execute(new ByOne<String>("id", id, "client", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", id, "client", "surname"));
      assertThat(actualSurname).isNull();
    }

    String expectedName2 = "name2 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName2)
      .commit();

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

    exampleUpserter.get().client(id)
      .name(expectedName3)
      .surname(expectedSurname)
      .commit();

    {
      String actualName = jdbc.get().execute(new ByOne<String>("id", id, "client", "name"));
      assertThat(actualName).isEqualTo(expectedName3);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", id, "client", "surname"));
      assertThat(actualSurname).isEqualTo(expectedSurname);
    }
  }

  @Test(expectedExceptions = CannotBeNull.class)
  public void upsertClient_CannotBeNull_surname() throws Exception {

    exampleUpserter.get().client(RND.plusLong(1_000_000_000L))
      .name("asd")
      .surname(null)
      .commit();

  }

  @Test
  public void insertSomeData() throws Exception {

    long chairId1_1 = RND.plusLong(1_000_000_000_000L);
    String chairId2_1 = RND.str(10);
    long chairId1_2 = RND.plusLong(1_000_000_000_000L);
    String chairId2_2 = RND.str(10);

    exampleUpserter.get().chair(chairId1_1, chairId2_1)
      .name(RND.str(10))
      .commit()
    ;

    exampleUpserter.get().chair(chairId1_2, chairId2_2)
      .name(RND.str(10))
      .commit()
    ;

    long clientId = RND.plusLong(1_000_000_000_000L);

    exampleUpserter.get().client(clientId)
      .name(RND.str(10))
      .surname(RND.str(10))
      .myChairId1(chairId1_1)
      .myChairId2(chairId2_1)
      .hisChairLongId(chairId1_2)
      .hisChairStrId(chairId2_2)
      .commit()
    ;
  }
}
