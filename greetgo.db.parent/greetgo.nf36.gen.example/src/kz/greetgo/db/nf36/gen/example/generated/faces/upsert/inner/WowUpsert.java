package kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner;

import java.lang.String;
import kz.greetgo.db.nf36.gen.example.generated.faces.upsert.inner.WowUpsert;

public interface WowUpsert {
  WowUpsert hello(String hello);

  WowUpsert more(String wowId, String wowId2);

  void commit();
}
