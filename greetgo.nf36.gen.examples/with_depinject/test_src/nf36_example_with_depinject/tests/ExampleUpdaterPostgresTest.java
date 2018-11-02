package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.AuthorGetterImpl;
import nf36_example_with_depinject.generated.faces.ExampleUpdater;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.jdbc.ByOne;
import nf36_example_with_depinject.jdbc.ByOneLast;
import nf36_example_with_depinject.structure.SomeEnum;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class ExampleUpdaterPostgresTest extends ParentDbTests {

  public BeanGetter<AuthorGetterImpl> authorGetterImpl;

  public BeanGetter<ExampleUpserter> exampleUpserter;

  public BeanGetter<ExampleUpdater> exampleUpdater;

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

    exampleUpdater.get().client()
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

    exampleUpdater.get().stone().whereIdIsEqualTo(id).setActual(false).commit();

  }

  @Test
  public void update_EntityEnumAsId() {
    String value = RND.str(10);
    SomeEnum id = SomeEnum.V2;

    exampleUpserter.get()
        .entityEnumAsId(id)
        .value(RND.str(10))
        .commit()
    ;

    assertThat(loadStr("id", id.name(), "entity_enum_as_id", "value")).isNotEqualTo(value);

    exampleUpdater.get()
        .entityEnumAsId()
        .setValue(value)
        .whereIdIsEqualTo(id)
        .commit()
    ;

    assertThat(loadStr("id", id.name(), "entity_enum_as_id", "value")).isEqualTo(value);
  }

  @Test
  public void updating_last_modified_at_on_update() {

    Date time1 = now();
    sleep(30);

    long id = exampleUpserter.get().clientNextId();

    exampleUpserter.get().client(id)
        .surname(RND.str(10))
        .name(RND.str(10))
        .commitAll();

    Date mod1 = loadDate("id", id, "client", "mod_at");

    sleep(30);
    Date time2 = now();
    sleep(30);

    exampleUpdater.get().client()
        .whereIdIsEqualTo(id)
        .setName(RND.str(10))
        .commitAll();

    Date mod2 = loadDate("id", id, "client", "mod_at");

    sleep(30);
    Date time3 = now();
    sleep(30);

    // time1 ... mod1 ... time2 ... mod2 ... time3

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

    System.out.println("update: time1 = " + sdf.format(time1));
    System.out.println("update:  mod1 = " + sdf.format(mod1));
    System.out.println("update: time2 = " + sdf.format(time2));
    System.out.println("update:  mod2 = " + sdf.format(mod2));
    System.out.println("update: time3 = " + sdf.format(time3));

    assertThat(mod1).isBetween(time1, time2, false, false);
    assertThat(mod2).isBetween(time2, time3, false, false);

  }
}
