package kz.greetgo.db.nf36.adapters;

import java.sql.Connection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;

class JdbcNf36UpserterAdapterPostgres extends JdbcNf36UpserterAbstractAdapter {

  @Override
  protected JdbcNf36UpserterAbstractAdapter copyInstance() {
    return new JdbcNf36UpserterAdapterPostgres();
  }

  protected void upsert(Connection con) throws Exception {
    String insertNames = "";
    String insertQ = "";
    String updateNamesQ = "";
    Stream<Object> insertStream = null;
    Stream<Object> updateStream = null;

    if (nf3CreatedBy != null) {
      insertNames = ", " + nf3CreatedBy + ", " + nf3ModifiedBy;
      insertQ = ", ?, ?";
      insertStream = Stream.of(author, author);
      updateNamesQ = ", " + nf3ModifiedBy + " = ?";
      updateStream = Stream.of(author);
    }

    String sql = "insert into " + nf3TableName + " ("
        + concat(idValueMap.keySet().stream().sorted(), fieldValueMap.keySet().stream().sorted())
        .collect(joining(", "))
        + insertNames
        + ") values ("
        + concat(idValueMap.keySet().stream().map(k -> "?"), fieldValueMap.keySet().stream().map(k -> "?"))
        .collect(joining(", "))
        + insertQ
        + ") on conflict ("
        + idValueMap.keySet().stream().sorted().collect(joining(", "))
        + ") do " +

        (fieldValueMap.isEmpty() ? "nothing" :

            "update set "
                + fieldValueMap.keySet().stream().sorted().map(k -> k + " = ?").collect(joining(", ")) + (

                toNowFieldList.isEmpty()
                    ? ""
                    : ", " + toNowFieldList.stream().map(n -> n + " = clock_timestamp()").collect(joining(", "))

            ) + updateNamesQ
        );

    Stream<Object> s = idValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue);
    s = concat(s, fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue));
    if (insertStream != null) {
      s = concat(s, insertStream);
    }
    s = concat(s, fieldValueMap.entrySet().stream().sorted(comparing(Map.Entry::getKey)).map(Map.Entry::getValue));
    if (updateStream != null && !fieldValueMap.isEmpty()) {
      s = concat(s, updateStream);
    }

    executeUpdate(con, sql, s.collect(Collectors.toList()));
  }
}
