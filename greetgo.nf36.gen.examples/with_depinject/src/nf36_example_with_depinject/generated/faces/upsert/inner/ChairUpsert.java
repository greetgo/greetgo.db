package nf36_example_with_depinject.generated.faces.upsert.inner;

public interface ChairUpsert {
  ChairUpsert name(String name);

  ChairUpsert description(String description);

  ChairUpsert more(long id1, String id2);

  void commit();
}
