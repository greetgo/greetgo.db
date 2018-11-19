package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf3Description;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.utils.UtilsNf36;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

class Nf3TableImpl implements Nf3Table {
  private final Object definer;
  private final ModelCollector owner;
  final List<Nf3FieldImpl> fields;

  public Nf3TableImpl(ModelCollector owner, Object definer) {
    this.definer = definer;
    this.owner = owner;

    fields = Arrays.stream(definer.getClass().getFields())
        .map(f -> new Nf3FieldImpl(this, definer, f, owner))
        .filter(Nf3FieldImpl::notIgnoring)
        .collect(Collectors.toList());
  }

  @Override
  public Class<?> source() {
    return definer.getClass();
  }

  @Override
  public List<Nf3Field> fields() {
    return unmodifiableList(fields);
  }

  @Override
  public String nf3prefix() {
    return owner.nf3prefix;
  }

  @Override
  public String nf6prefix() {
    return owner.nf6prefix;
  }

  @Override
  public String tableName() {
    String name = UtilsNf36.javaNameToDbName(definer.getClass().getSimpleName());
    return name.startsWith("_") ? name.substring(1) : name;
  }

  @Override
  public String commentQuotedForSql() {
    Nf3Description d = definer.getClass().getAnnotation(Nf3Description.class);
    if (d == null) {
      throw new RuntimeException("No description for class " + definer.getClass().getSimpleName());
    }
    return UtilsNf36.quoteForSql(d.value());
  }

  @Override
  public String toString() {
    return "Nf3Table{" + tableName() + "}";
  }
}
