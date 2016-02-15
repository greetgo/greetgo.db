package kz.greetgo.gbatis.util.callbacks;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.gbatis.util.model.Colinfo;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public final class InsertCallback implements ConnectionCallback<Void> {
  public SqlViewer sqlViewer;
  
  private final String tableName;
  private final Object object;
  private final Map<String, Colinfo> colinfoMap;
  
  public InsertCallback(String tableName, Collection<Colinfo> colinfo, Object object) {
    this.tableName = tableName;
    this.object = object;
    
    colinfoMap = new HashMap<>();
    for (Colinfo s : colinfo) {
      colinfoMap.put(s.name.toLowerCase(), s);
    }
  }
  
  public InsertCallback(SqlViewer sqlViewer, String tableName, Collection<Colinfo> colinfo,
      Object object) {
    this(tableName, colinfo, object);
    this.sqlViewer = sqlViewer;
  }
  
  @Override
  public Void doInConnection(Connection con) throws SQLException, DataAccessException {
    if (object == null) return null;
    
    class Data {
      final Object value;
      @SuppressWarnings("unused")
      final Colinfo colinfo;
      
      public Data(Object value, Colinfo colinfo) {
        this.value = value;
        this.colinfo = colinfo;
      }
    }
    
    List<Data> data = new ArrayList<>();
    
    StringBuilder sql = new StringBuilder();
    sql.append("insert into ").append(tableName).append(" (");
    
    boolean needComma = false;
    for (Field field : object.getClass().getFields()) {
      String fieldLowName = field.getName().toLowerCase();
      if (!colinfoMap.keySet().contains(fieldLowName)) continue;
      final Object value;
      try {
        value = field.get(object);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      if (value != null || colinfoMap.get(fieldLowName).canNull) {
        if (needComma) {
          sql.append(", ");
        } else {
          needComma = true;
        }
        sql.append(field.getName());
        data.add(new Data(value, colinfoMap.get(fieldLowName)));
      }
    }
    
    if (data.size() == 0) {
      throw new IllegalArgumentException("No data to insert into " + tableName
          + " from object of class " + object.getClass());
    }
    
    sql.append(") values (?");
    for (int i = 1, C = data.size(); i < C; i++) {
      sql.append(", ?");
    }
    sql.append(')');
    
    long startedAt = System.currentTimeMillis();
    List<Object> params = new ArrayList<>();
    try {
      try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
        {
          int index = 1;
          for (Data x : data) {
            ps.setObject(index++, SqlUtil.forSql(x.value));
            params.add(x.value);
          }
        }
        
        ps.executeUpdate();
        if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, null, sql, params);
        return null;
      }
      
    } catch (Exception e) {
      if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, e, sql, params);
      throw e;
    }
  }
}