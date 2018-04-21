package kz.greetgo.db.nf36.model;

import java.util.List;

public interface Nf3Table {
  Class<?> source();

  String tableName();

  List<Nf3Field> fields();

  String nf3prefix();

  String nf6prefix();

  default String getDbNameByJavaName(String javaName) {
    if (javaName == null) throw new IllegalArgumentException("javaName == null");
    return fields().stream()
      .filter(f -> javaName.equals(f.javaName()))
      .map(Nf3Field::dbName)
      .findAny()
      .orElseThrow(() -> new RuntimeException("No field " + javaName + " in " + source().getSimpleName()));
  }

  default Nf3Field getByJavaName(String javaName) {
    if (javaName == null) throw new IllegalArgumentException("javaName == null");
    return fields().stream()
      .filter(f -> javaName.equals(f.javaName()))
      .findAny()
      .orElseThrow(() -> new RuntimeException("No field " + javaName + " in " + source().getSimpleName()));
  }
}
