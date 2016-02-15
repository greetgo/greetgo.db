package kz.greetgo.gbatis.util.callbacks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.SqlUtil;
import kz.greetgo.gbatis.util.model.Colinfo;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

public final class GetFieldCallback<T> implements ConnectionCallback<T> {
  public SqlViewer sqlViewer;
  
  private final String tableName;
  private final Object[] fieldValuePairs;
  private final String gettingField;
  private final Map<String, Colinfo> colinfoMap;
  private final Class<T> classs;
  
  public GetFieldCallback(Class<T> classs, String tableName, List<Colinfo> colinfo,
      String gettingField, Object... fieldValuePairs) {
    this.classs = classs;
    this.tableName = tableName;
    this.fieldValuePairs = fieldValuePairs;
    this.gettingField = gettingField;
    
    colinfoMap = new HashMap<>();
    for (Colinfo x : colinfo) {
      colinfoMap.put(x.name.toLowerCase(), x);
    }
  }
  
  public GetFieldCallback(SqlViewer sqlViewer, Class<T> classs, String tableName,
      List<Colinfo> colinfo, String gettingField, Object... fieldValuePairs) {
    this(classs, tableName, colinfo, gettingField, fieldValuePairs);
    this.sqlViewer = sqlViewer;
  }
  
  @Override
  public T doInConnection(Connection con) throws SQLException, DataAccessException {
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
    sql.append("select ").append(gettingField).append(" from ").append(tableName)
        .append(" where 1=1");
    for (int i = 0, C = fieldValuePairs.length / 2; i < C; i++) {
      String field = (String)fieldValuePairs[2 * i + 0];
      Object value = fieldValuePairs[2 * i + 1];
      if (value == null) {
        sql.append(" and ").append(field).append(" is null");
      } else {
        sql.append(" and ").append(field).append(" = ?");
        data.add(new Data(value, colinfoMap.get(field.toLowerCase())));
      }
    }
    
    long startedAt = System.currentTimeMillis();
    List<Object> params = new ArrayList<>();
    try {
      PreparedStatement ps = con.prepareStatement(sql.toString());
      try {
        {
          int index = 1;
          for (Data d : data) {
            ps.setObject(index++, SqlUtil.forSql(d.value));
            params.add(d.value);
          }
        }
        ResultSet rs = ps.executeQuery();
        try {
          
          Object ret = null;
          if (rs.next()) ret = rs.getObject(1);
          
          if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, null, sql, params);
          
          return SqlUtil.fromSql(ret, classs);
          
        } finally {
          rs.close();
        }
      } finally {
        ps.close();
        
      }
      
    } catch (Exception e) {
      if (U.need(sqlViewer)) U.view(startedAt, sqlViewer, e, sql, params);
      throw e;
    }
  }
}