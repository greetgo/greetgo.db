package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.gen.example.beans.AuthorGetterImpl;
import kz.greetgo.db.nf36.gen.example.beans.BeanConfigAll;
import kz.greetgo.db.nf36.gen.example.generated.faces.ExampleUpserter;
import kz.greetgo.db.nf36.gen.example.jdbc.ByOne;
import kz.greetgo.db.nf36.gen.example.jdbc.ByOneCount;
import kz.greetgo.db.nf36.gen.example.jdbc.ByOneLast;
import kz.greetgo.db.nf36.gen.example.jdbc.ByTwoCount;
import kz.greetgo.db.nf36.gen.example.jdbc.ByTwoLast;
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

  public BeanGetter<AuthorGetterImpl> authorGetterImpl;

  @Test
  public void upsertClient() throws Exception {

    authorGetterImpl.get().author = "Сталина на вас нет!";

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

  @Test
  public void upsertClient_NF6() throws Exception {
    long id = RND.plusLong(1_000_000_000_000L);
    String expectedName1 = "name1 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commit();

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commit();

    {
      String actualName = jdbc.get().execute(new ByOneLast<>("id", id, "m_client_name", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
      int count = jdbc.get().execute(new ByOneCount("id", id, "m_client_name"));
      assertThat(count).isEqualTo(1);
    }

    String expectedName2 = "name2 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName2)
      .commit();

    exampleUpserter.get().client(id)
      .name(expectedName2)
      .commit();

    {
      String actualName = jdbc.get().execute(new ByOneLast<>("id", id, "m_client_name", "name"));
      assertThat(actualName).isEqualTo(expectedName2);
      int count = jdbc.get().execute(new ByOneCount("id", id, "m_client_name"));
      assertThat(count).isEqualTo(2);
    }

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commit();

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commit();

    {
      String actualName = jdbc.get().execute(new ByOneLast<>("id", id, "m_client_name", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
      int count = jdbc.get().execute(new ByOneCount("id", id, "m_client_name"));
      assertThat(count).isEqualTo(3);
    }
  }

  @Test
  public void upsertChair_NF6() throws Exception {
    long id1 = RND.plusLong(1_000_000_000_000L);
    String id2 = RND.str(10);
    String expectedName1 = "name1 " + RND.str(10);
    String expectedDescription1 = "description1 " + RND.str(10);

    exampleUpserter.get().chair(id1, id2)
      .name(expectedName1)
      .description("left " + RND.str(10))
      .commit()
    ;

    {
      int countName = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_name"));
      assertThat(countName).isEqualTo(1);
      int countDescription = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_description"));
      assertThat(countDescription).isEqualTo(1);
    }

    exampleUpserter.get().chair(id1, id2)
      .name(expectedName1)
      .description(expectedDescription1)
      .commit()
    ;

    {
      String actualName = jdbc.get().execute(new ByTwoLast<>("id1", id1, "id2", id2, "m_chair_name", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
      String actualDescription = jdbc.get().execute(new ByTwoLast<>("id1", id1, "id2", id2, "m_chair_description", "description"));
      assertThat(actualDescription).isEqualTo(expectedDescription1);

      int countName = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_name"));
      assertThat(countName).isEqualTo(1);
      int countDescription = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_description"));
      assertThat(countDescription).isEqualTo(2);
    }

    String expectedName2 = "name2 " + RND.str(10);
    String expectedDescription2 = "description2 " + RND.str(10);

    exampleUpserter.get().chair(id1, id2)
      .name("left name " + RND.str(10))
      .description(expectedDescription2)
      .commit()
    ;

    {
      int countName = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_name"));
      assertThat(countName).isEqualTo(2);
      int countDescription = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_description"));
      assertThat(countDescription).isEqualTo(3);
    }

    exampleUpserter.get().chair(id1, id2)
      .name(expectedName2)
      .description(expectedDescription2)
      .commit()
    ;

    {
      String actualName = jdbc.get().execute(new ByTwoLast<>("id1", id1, "id2", id2, "m_chair_name", "name"));
      assertThat(actualName).isEqualTo(expectedName2);
      String actualDescription = jdbc.get().execute(new ByTwoLast<>("id1", id1, "id2", id2, "m_chair_description", "description"));
      assertThat(actualDescription).isEqualTo(expectedDescription2);

      int countName = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_name"));
      assertThat(countName).isEqualTo(3);
      int countDescription = jdbc.get().execute(new ByTwoCount("id1", id1, "id2", id2, "m_chair_description"));
      assertThat(countDescription).isEqualTo(3);
    }
  }

  @Test
  public void upsertClient_chair() throws Exception {
    long chairId1 = RND.plusLong(1_000_000_000_000L);
    String chairId2 = RND.str(10);

    exampleUpserter.get().chair(chairId1, chairId2)
      .name(RND.str(10))
      .commit()
    ;

    long clientId = RND.plusLong(1_000_000_000_000L);

    exampleUpserter.get().client(clientId)
      .myChairId1(chairId1)
      .myChairId2(chairId2)
      .commit()
    ;

    {
      long actual = jdbc.get().execute(new ByOneLast<>("id", clientId, "m_client_my_chair_id1", "my_chair_id1"));
      assertThat(actual).isEqualTo(chairId1);
    }
    {
      String actual = jdbc.get().execute(new ByOneLast<>("id", clientId, "m_client_my_chair_id1", "my_chair_id2"));
      assertThat(actual).isEqualTo(chairId2);
    }
  }
}
