package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.AuthorGetterImpl;
import nf36_example_with_depinject.generated.faces.ExampleHistorySelector;
import nf36_example_with_depinject.generated.faces.ExampleUpserter;
import nf36_example_with_depinject.structure.Client;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.Test;

import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class ExampleHistorySelectorPostgresTest extends ParentDbTests {

  public BeanGetter<ExampleUpserter> exampleUpserter;

  public BeanGetter<AuthorGetterImpl> authorGetterImpl;

  public BeanGetter<ExampleHistorySelector> exampleHistorySelector;

  // TODO: 19.11.18 place to continue after generating

  @Test
  public void selectHistoryOfClient() throws Exception {

    authorGetterImpl.get().author = "Сталина на вас нет!";

    long id = RND.plusLong(1_000_000_000_000L);
    String expectedName = "name " + RND.str(10);

    exampleUpserter.get().client(id)
        .name(expectedName)
        .commitAll();

    sleep(50);

    Date date1 = now();

    sleep(50);

    exampleUpserter.get().client(id)
        .name(RND.str(10))
        .commitAll();

    Client client = exampleHistorySelector.get().client()
        .name()
        .at(date1)
        .get(id);

    assertThat(client).isNotNull();
    assertThat(client.name).isEqualTo(expectedName);
  }
}
