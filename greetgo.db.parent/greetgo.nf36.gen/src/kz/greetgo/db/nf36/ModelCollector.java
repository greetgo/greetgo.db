package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Table;

import java.util.ArrayList;
import java.util.List;

public class ModelCollector {

  String nf3prefix;
  String nf6prefix;

  private ModelCollector() {
  }

  public static ModelCollector newCollector() {
    return new ModelCollector();
  }

  public ModelCollector setNf3Prefix(String nf3prefix) {
    this.nf3prefix = nf3prefix;
    return this;
  }

  public ModelCollector setNf6Prefix(String nf6prefix) {
    this.nf6prefix = nf6prefix;
    return this;
  }

  final List<Object> registeredObjects = new ArrayList<>();

  public ModelCollector register(Object object) {
    registeredObjects.add(object);
    return this;
  }

  private boolean collected = false;

  public List<Nf3Table> collect() {
    if (collected) throw new RuntimeException("Already collected");
    collected = true;

    List<Nf3Table> ret = new ArrayList<>();

    for (Object object : registeredObjects) {
      ret.add(new Nf3Table() {

        @Override
        public Class<?> source() {
          return object.getClass();
        }
      });
    }

    return ret;
  }
}
