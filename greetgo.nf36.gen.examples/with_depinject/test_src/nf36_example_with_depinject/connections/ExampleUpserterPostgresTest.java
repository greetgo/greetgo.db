package nf36_example_with_depinject.connections;

import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.beans.all.AuthorGetterImpl;
import nf36_example_with_depinject.beans.postgres.BeanConfigPostgres;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.jdbc.*;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigPostgres.class)
public class ExampleUpserterPostgresTest extends AbstractDepinjectTestNg {

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
      .commitAll();

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
      .commitAll();

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
      .commitAll();

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
      .commitAll();

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
      .commitAll()
    ;
  }

  @Test
  public void upsertClient_NF6() throws Exception {
    long id = RND.plusLong(1_000_000_000_000L);
    String expectedName1 = "name1 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commitAll();

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commitAll();

    {
      String actualName = jdbc.get().execute(new ByOneLast<>("id", id, "m_client_name", "name"));
      assertThat(actualName).isEqualTo(expectedName1);
      int count = jdbc.get().execute(new ByOneCount("id", id, "m_client_name"));
      assertThat(count).isEqualTo(1);
    }

    String expectedName2 = "name2 " + RND.str(10);

    exampleUpserter.get().client(id)
      .name(expectedName2)
      .commitAll();

    exampleUpserter.get().client(id)
      .name(expectedName2)
      .commitAll();

    {
      String actualName = jdbc.get().execute(new ByOneLast<>("id", id, "m_client_name", "name"));
      assertThat(actualName).isEqualTo(expectedName2);
      int count = jdbc.get().execute(new ByOneCount("id", id, "m_client_name"));
      assertThat(count).isEqualTo(2);
    }

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commitAll();

    exampleUpserter.get().client(id)
      .name(expectedName1)
      .commitAll();

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
      .commitAll()
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

  @Test
  public void upsertMore() throws Exception {
    long clientId1 = RND.plusLong(1_000_000_000_000L);
    long clientId2 = RND.plusLong(1_000_000_000_000L);

    String surname1 = RND.str(10);
    String surname2 = RND.str(10);

    exampleUpserter.get().client(clientId1)
      .surname(surname1)
      .moreAnother(clientId2)
      .surname(surname2)
      .commitAll();

    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", clientId1, "client", "surname"));
      assertThat(actualSurname).isEqualTo(surname1);
    }
    {
      String actualSurname = jdbc.get().execute(new ByOne<String>("id", clientId2, "client", "surname"));
      assertThat(actualSurname).isEqualTo(surname2);
    }
  }
}