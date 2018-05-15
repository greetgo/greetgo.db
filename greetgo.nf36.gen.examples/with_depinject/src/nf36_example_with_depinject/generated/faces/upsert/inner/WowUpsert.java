package nf36_example_with_depinject.generated.faces.upsert.inner;

import java.lang.String;
import nf36_example_with_depinject.generated.faces.upsert.inner.WowUpsert;

public interface WowUpsert {
  WowUpsert hello(String hello);

  WowUpsert more(String wowId, String wowId2);

  void commit();
}
