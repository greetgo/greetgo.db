package kz.greetgo.db.nf36.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface Nf3Table {
  Class<?> source();

  String tableName();

  List<Nf3Field> fields();

  String nf3prefix();

  String nf6prefix();

  default String getDbNameByJavaName(String javaName) {
    if (javaName == null) {
      throw new IllegalArgumentException("javaName == null");
    }
    return fields().stream()
        .filter(f -> javaName.equals(f.javaName()))
        .map(Nf3Field::dbName)
        .findAny()
        .orElseThrow(() -> new RuntimeException("No field " + javaName + " in " + source().getSimpleName()));
  }

  default Nf3Field getByJavaName(String javaName) {
    if (javaName == null) {
      throw new IllegalArgumentException("javaName == null");
    }
    return fields().stream()
        .filter(f -> javaName.equals(f.javaName()))
        .findAny()
        .orElseThrow(() -> new RuntimeException("No field " + javaName + " in " + source().getSimpleName()));
  }

  default String nf3TableName() {
    return nf3prefix() + tableName();
  }

  default String commaSeparatedIdDbNames() {
    return fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::dbName)
        .collect(Collectors.joining(", "));
  }

  String commentQuotedForSql();
}
