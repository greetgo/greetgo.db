package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.nf36.utils.SqlConvertUtil;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static kz.greetgo.db.nf36.utils.UtilsNf36.cat;
import static kz.greetgo.db.nf36.utils.UtilsNf36.selectName;

class JdbcNf36UpserterAdapterOracle extends JdbcNf36UpserterAbstractAdapter {
  @Override
  protected JdbcNf36UpserterAbstractAdapter copyInstance() {
    return new JdbcNf36UpserterAdapterOracle();
  }

  @Override
  protected void upsert(Connection con) throws Exception {

    List<String> idNames = idValueMap.keySet().stream()
        .sorted()
        .collect(toList());
    List<Object> idValues = idValueMap.entrySet().stream()
        .sorted(Comparator.comparing(Map.Entry::getKey))
        .map(Map.Entry::getValue)
        .collect(toList());

    List<String> flNames = fieldValueMap.keySet().stream().sorted().collect(toList());
    List<Object> flValues = fieldValueMap.entrySet().stream()
        .sorted(Comparator.comparing(Map.Entry::getKey))
        .map(Map.Entry::getValue).collect(toList());

    List<String> authNames = emptyList();
    List<Object> authValues = emptyList();

    List<String> insNames = emptyList();
    List<String> insValues = emptyList();

    List<String> updates = emptyList();

    if (nf3CreatedBy != null) {
      insNames = Arrays.asList(nf3CreatedBy, nf3ModifiedBy);
      String authorVar = selectName("author", cat(idNames, flNames, insNames).collect(toSet()));
      insValues = Arrays.asList(authorVar, authorVar);

      updates = singletonList("d." + nf3ModifiedBy + " = s." + authorVar);

      authNames = singletonList(authorVar);
      authValues = singletonList(author);
    }

    if (flNames.isEmpty()) {
      updates = emptyList();
    }

    String sql = "merge into " + nf3TableName + " d using (select " + (

        cat(idNames, flNames, authNames)
            .map(s -> "? as " + s)
            .collect(Collectors.joining(", "))

    ) + " from dual) s on (" + (

        cat(idNames).map(n -> "s." + n + " = d." + n).collect(joining(" and "))

    ) + ")" + (flNames.size() + updates.size() == 0 ? "" : " when matched then update set " + (

        cat(cat(flNames).map(n -> "d." + n + " = s." + n).collect(toList()), updates).collect(joining(", "))

            + (

            toNowFieldList.isEmpty()
                ? ""
                : ", " + toNowFieldList.stream().map(n -> "d." + n + " = localtimestamp").collect(joining(", "))

        )

    )) + " when not matched then insert (" + (
        cat(idNames, flNames, insNames).map(n -> "d." + n).collect(joining(", "))
    ) + ") values (" + (
        cat(idNames, flNames, insValues).map(n -> "s." + n).collect(joining(", "))
    ) + ")";

    executeUpdate(con, sql, cat(idValues, flValues, authValues).collect(toList()));
  }

  @Override
  protected boolean arrayEquals(List<Object> list1, List<Object> list2) {
    if (list1 == null && list2 == null) {
      return true;
    }
    if (list1 == null || list2 == null) {
      return false;
    }

    int size = list1.size();
    if (size != list2.size()) {
      return false;
    }

    for (int i = 0; i < size; i++) {

      Object v1 = SqlConvertUtil.fromSql(list1.get(i));
      Object v2 = SqlConvertUtil.fromSql(list2.get(i));

      if (!Objects.equals(v1, v2)) {
        return false;
      }
    }

    return true;
  }

}
