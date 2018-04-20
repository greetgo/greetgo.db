package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.structure.Client;
import kz.greetgo.db.nf36.structure.ClientAddress;
import kz.greetgo.db.nf36.structure.Street;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;

public class ModelCollectorTest {
  @Test
  public void testName() throws Exception {
    Map<String, Nf3Table> map = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .collect().stream()
      .collect(Collectors.toMap(a -> a.source().getSimpleName(), Function.identity()));

    assertThat(map).containsKey(Client.class.getSimpleName());
    Nf3Table client = map.get(Client.class.getSimpleName());

    System.out.println(client);
  }
}