package kz.greetgo.gbatis.util.callbacks;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.gbatis.util.model.ColInfo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

@SuppressFBWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public final class UpdateCallback implements ConnectionCallback<Integer> {
  public SqlViewer sqlViewer;

  private final String tableName;
  private final Object object;
  private final Map<String, ColInfo> colinfoMap;
  private final Set<String> keyNamesInLowcase;

  public UpdateCallback(String tableName, Collection<ColInfo> colInfo, Collection<String> keyNames,
                        Object object) {
    this.tableName = tableName;
    this.object = object;

    if (object == null) throw new IllegalArgumentException("object == null");

    colinfoMap = new HashMap<>();
    for (ColInfo s : colInfo) {
      colinfoMap.put(s.name.toLowerCase(), s);
    }

    keyNamesInLowcase = new HashSet<>();
    for (String s : keyNames) {
      keyNamesInLowcase.add(s.toLowerCase());
    }
  }

  public UpdateCallback(SqlViewer sqlViewer, String tableName, Collection<ColInfo> colInfo,
                        Collection<String> keyNames, Object object) {
    this(tableName, colInfo, keyNames, object);
    this.sqlViewer = sqlViewer;
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {
    if (object == null) return 0;

    class Data {
      final Object value;
      @SuppressWarnings("unused")
      final ColInfo colinfo;

      public Data(Object value, ColInfo colinfo) {
        this.value = value;
        this.colinfo = colinfo;
      }
    }

    List<Data> data = new ArrayList<>();
    List<Data> keyData = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(tableName).append(" set");

    StringBuilder where = new StringBuilder();
    where.append(" where 1=1");

    boolean needComma = false;
    for (Field field : object.getClass().getFields()) {
      String fn = field.getName().toLowerCase();
      if (keyNamesInLowcase.contains(fn)) {
        where.append(" and ").append(field.getName()).append(" = ?");
        final Object value;
        try {
          value = field.get(object);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        if (value == null) throw new IllegalArgumentException("It is updating table " + tableName
          + ". This table has primary key with field " + field.getName()
          + ". But its value in object with class " + object.getClass()
          + " is null. I do not know what to update.");
        keyData.add(new Data(value, colinfoMap.get(fn)));
      } else if (colinfoMap.keySet().contains(fn)) {
        final Object value;
        try {
          value = field.get(object);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        if (value != null || colinfoMap.get(fn).canNull) {
          if (needComma) {
            sql.append(", ");
          } else {
            needComma = true;
          }
          sql.append(' ').append(field.getName()).append(" = ?");

          data.add(new Data(value, colinfoMap.get(fn)));
        }
      }
    }

    if (keyData.size() == 0) {
      throw new IllegalArgumentException("No key values in update of table " + tableName
        + " from object of class " + object.getClass());
    }

    if (data.size() == 0) {
      throw new IllegalArgumentException("Nothing to update in table " + tableName
        + " from object of class " + object.getClass());
    }

    sql.append(where);

    List<Object> params = new ArrayList<>();
    long startedAt = System.currentTimeMillis();
    try {
      try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
        int index = 1;
        for (Data d : data) {
          ps.setObject(index++, SqlUtil.forSql(d.value));
          params.add(d.value);
        }
        for (Data d : keyData) {
          ps.setObject(index++, SqlUtil.forSql(d.value));
          params.add(d.value);
        }
        {
          int ret = ps.executeUpdate();
          if (ret == 0) throw new NoUpdateException("PreparedStatement.executeUpdate() returns"
            + " 0 when updating table " + tableName + " by object of " + object.getClass());

          if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, null, sql, params);

          return ret;
        }
      }
    } catch (Exception e) {
      if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, e, sql, params);
      throw e;
    }
  }
}