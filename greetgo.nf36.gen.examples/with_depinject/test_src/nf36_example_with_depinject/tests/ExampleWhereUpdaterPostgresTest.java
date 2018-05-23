package nf36_example_with_depinject.tests;

import kz.greetgo.db.Jdbc;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.AuthorGetterImpl;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.generated.faces.ExampleWhereUpdater;
import nf36_example_with_depinject.jdbc.ByOne;
import nf36_example_with_depinject.jdbc.ByOneLast;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class ExampleWhereUpdaterPostgresTest extends ParentDbTests {

  public BeanGetter<AuthorGetterImpl> authorGetterImpl;

  public BeanGetter<ExampleUpserter> exampleUpserter;

  public BeanGetter<ExampleWhereUpdater> exampleWhereUpdater;

  public BeanGetter<Jdbc> jdbc;

  @Test
  public void test_exampleWhereUpdater_mainWork() {
    authorGetterImpl.get().author = "Создатель";

    String patronymic = "pater " + RND.str(10);

    long id1 = RND.plusLong(1_000_000_000_000L);
    String name1 = "name1 " + RND.str(10);
    String surname1 = "sur1 " + RND.str(10);

    exampleUpserter.get().client(id1)
      .name(name1)
      .surname(surname1)
      .patronymic(patronymic)
      .commitAll();

    long id2 = RND.plusLong(1_000_000_000_000L);
    String name2 = "name2 " + RND.str(10);
    String surname2 = "sur2 " + RND.str(10);

    exampleUpserter.get().client(id2)
      .name(name2)
      .surname(surname2)
      .patronymic(patronymic)
      .commitAll();

    long id3 = RND.plusLong(1_000_000_000_000L);

    exampleUpserter.get().client(id3)
      .name("Александр")
      .patronymic("Сергеевич")
      .surname("Пушкин")
      .commitAll();

    String newName = "newName " + RND.str(10);

    authorGetterImpl.get().author = "Менятель";

    exampleWhereUpdater.get().client()
      .setName(newName)
      .wherePatronymicIsEqualTo(patronymic)
      .commitAll();

    {
      String actualName = jdbc.get().execute(new ByOne<>("id", id1, "client", "name"));
      assertThat(actualName).isEqualTo(newName);
    }
    {
      String actualName = jdbc.get().execute(new ByOne<>("id", id2, "client", "name"));
      assertThat(actualName).isEqualTo(newName);
    }
    {
      String actualName = jdbc.get().execute(new ByOne<>("id", id3, "client", "name"));
      assertThat(actualName).isEqualTo("Александр");
    }

    {
      String author = jdbc.get().execute(new ByOneLast<>("id", id1, t("m_client_name"), "inserted_by"));
      assertThat(author).isEqualTo("Менятель");
    }
    {
      String author = jdbc.get().execute(new ByOneLast<>("id", id2, t("m_client_name"), "inserted_by"));
      assertThat(author).isEqualTo("Менятель");
    }
    {
      String author = jdbc.get().execute(new ByOneLast<>("id", id3, t("m_client_name"), "inserted_by"));
      assertThat(author).isEqualTo("Создатель");
    }
  }

  @Test
  public void test_booleanSet() {

    String id = RND.str(10);

    exampleUpserter.get().stone(id)
      .name(RND.str(10))
      .actual(true)
      .commit();

    exampleWhereUpdater.get().stone().whereIdIsEqualTo(id).setActual(false).commit();

  }
}
