package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.nf36.model.SqlLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class JdbcNf36UpdaterAdapterPostgres extends JdbcNf36UpdaterAbstractAdapter {
  @Override
  protected List<IdValues> updateNf3(Connection con) throws Exception {

    String toNow = "";
    if (updateToNowFieldList.size() > 0) {
      toNow = ", " + updateToNowFieldList.stream().map(f -> f + " = clock_timestamp()").collect(joining(", "));
    }

    List<Object> params = setFieldList.stream().map(sf -> sf.fieldValue).collect(toList());

    String auth = "";
    if (nf3ModifiedBy != null) {
      auth = ", " + nf3ModifiedBy + " = ?";
      params.add(author);
    }

    params.addAll(whereList.stream().map(w -> w.fieldValue).collect(toList()));

    String sql = "update " + nf3TableName + " set " + (

        setFieldList.stream().map(sf -> sf.fieldName + " = ?").collect(Collectors.joining(", "))

    ) + toNow + auth + " where " + (

        whereList.stream().map(w -> w.fieldName + " = ?").collect(Collectors.joining(" and "))

    ) + " returning " + String.join(", ", idFieldNames);

    long startedAt = System.nanoTime();

    try (PreparedStatement ps = con.prepareStatement(sql)) {

      {
        int index = 1;
        for (Object param : params) {
          ps.setObject(index++, param);
        }
      }

      try (ResultSet rs = ps.executeQuery()) {

        List<IdValues> ret = new ArrayList<>();

        while (rs.next()) {

          ret.add(extractIdValues(rs));

        }

        if (logAcceptor != null && logAcceptor.isTraceEnabled()) {
          logAcceptor.accept(new SqlLog(sql, params, null, System.nanoTime() - startedAt));
        }

        return ret;

      }

    } catch (Exception e) {

      if (logAcceptor != null && logAcceptor.isErrorEnabled()) {
        logAcceptor.accept(new SqlLog(sql, params, e, System.nanoTime() - startedAt));
      }

      throw e;
    }
  }
}
