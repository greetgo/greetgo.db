package kz.greetgo.gbatis.util.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Creator;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.ResultType;
import kz.greetgo.gbatis.model.SqlWithParams;
import kz.greetgo.gbatis.util.OperUtil;
import kz.greetgo.gbatis.util.callbacks.CountWhereCallback;
import kz.greetgo.gbatis.util.callbacks.DeleteWhereCallback;
import kz.greetgo.gbatis.util.callbacks.GetFieldCallback;
import kz.greetgo.gbatis.util.callbacks.InsertCallback;
import kz.greetgo.gbatis.util.callbacks.NoUpdateException;
import kz.greetgo.gbatis.util.callbacks.UpdateCallback;
import kz.greetgo.gbatis.util.callbacks.meta.AllReferencesCallback;
import kz.greetgo.gbatis.util.callbacks.meta.ColinfoCallback;
import kz.greetgo.gbatis.util.callbacks.meta.KeyNamesCallback;
import kz.greetgo.gbatis.util.iface.UtilRegister;
import kz.greetgo.gbatis.util.model.Colinfo;
import kz.greetgo.gbatis.util.model.ForeignKey;
import kz.greetgo.gbatis.util.model.TableReference;
import kz.greetgo.util.db.DbTypeDetector;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractUtilRegister implements UtilRegister {
  
  protected abstract JdbcTemplate jdbc();
  
  protected abstract SqlViewer sqlViewer();
  
  @Override
  public void cleanTables(String... tableName) {
    Set<String> hashSet = new HashSet<String>();// Для избежания бесконечной
                                                // рекурсии
    for (String tn : tableName) {
      cleanTable(hashSet, tn);
    }
  }
  
  private void cleanTable(Set<String> nameSet, String tableName) {
    if (nameSet.contains(tableName)) return;
    nameSet.add(tableName);
    
    Set<ForeignKey> referencesTo = referencesTo(tableName);
    
    for (ForeignKey fk : referencesTo) {
      if (fk.canNull) {
        execUpdate("update " + fk.tableName + " set " + fk.colName + " = null");
      } else {
        cleanTable(nameSet, fk.tableName);
      }
    }
    
    deleteWhere(tableName, null);
  }
  
  private Map<String, Set<ForeignKey>> referenceMap = null;
  private final Object referenceMapSyncer = new Object();
  
  private Set<ForeignKey> referencesTo(String tableName) {
    
    synchronized (referenceMapSyncer) {
      if (referenceMap == null) {
        
        referenceMap = new HashMap<>();
        
        Set<TableReference> allReferences = jdbc().execute(new AllReferencesCallback(sqlViewer()));
        for (TableReference ref : allReferences) {
          Set<ForeignKey> set = referenceMap.get(ref.toTableName.toLowerCase());
          if (set == null) referenceMap.put(ref.toTableName.toLowerCase(), set = new HashSet<>());
          set.add(ref.from());
        }
        
      }
    }
    
    {
      Set<ForeignKey> ret = referenceMap.get(tableName.toLowerCase());
      return ret == null ? new HashSet<ForeignKey>() :ret;
    }
  }
  
  @Override
  public <T> T getField(Class<T> cl, final String tableName, final String gettingField,
      final Object... fieldValuePairs) {
    if (fieldValuePairs.length % 2 > 0) {
      throw new IllegalArgumentException("fieldValuePairs must contains odd elements");
    }
    return jdbc().execute(
        new GetFieldCallback<T>(sqlViewer(), cl, tableName, getColinfo(tableName), gettingField,
            fieldValuePairs));
  }
  
  @Override
  public <T> T insert(String tableName, T object) {
    jdbc().execute(new InsertCallback(sqlViewer(), tableName, getColinfo(tableName), object));
    return object;
  }
  
  private final Map<String, List<Colinfo>> colinfoCache = new HashMap<>();
  
  private synchronized List<Colinfo> getColinfo(String tableName) {
    {
      List<Colinfo> ret = colinfoCache.get(tableName);
      if (ret != null) return ret;
    }
    {
      List<Colinfo> ret = jdbc().execute(new ColinfoCallback(sqlViewer(), tableName));
      colinfoCache.put(tableName, ret);
      return ret;
    }
  }
  
  @Override
  public int update(String tableName, Object object) {
    return jdbc().execute(
        new UpdateCallback(tableName, getColinfo(tableName), getKeyNames(tableName), object));
  }
  
  @Override
  public boolean save(String tableName, Object object) {
    try {
      update(tableName, object);
      return false;
    } catch (NoUpdateException e) {
      insert(tableName, object);
      return true;
    }
  }
  
  private final Map<String, List<String>> keyNameCache = new HashMap<>();
  
  private synchronized List<String> getKeyNames(String tableName) {
    {
      List<String> ret = keyNameCache.get(tableName);
      if (ret != null) return ret;
    }
    {
      List<String> ret = jdbc().execute(new KeyNamesCallback(sqlViewer(), tableName));
      keyNameCache.put(tableName, ret);
      return ret;
    }
  }
  
  @Override
  public int deleteWhere(String tableName, String where, Object... values) {
    return jdbc().execute(new DeleteWhereCallback(sqlViewer(), tableName, where, values));
  }
  
  @Override
  public int countWhere(String tableName, String where, Object... values) {
    return jdbc().execute(new CountWhereCallback(sqlViewer(), tableName, where, values));
  }
  
  @Override
  public boolean existsKey(String tableName, Object object) {
    if (object == null) throw new IllegalArgumentException("object = null");
    
    Map<String, Field> fieldMap = new HashMap<>();
    for (Field field : object.getClass().getFields()) {
      fieldMap.put(field.getName().toLowerCase(), field);
    }
    
    List<Object> values = new ArrayList<>();
    
    StringBuilder where = new StringBuilder();
    for (String key : getKeyNames(tableName)) {
      Field field = fieldMap.get(key.toLowerCase());
      if (field == null) throw new IllegalArgumentException("No key field " + key
          + " in object with class " + object.getClass());
      
      final Object value;
      try {
        value = field.get(object);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      if (value == null) throw new IllegalArgumentException("Field " + key
          + " is null in object with class " + object.getClass());
      
      if (where.length() > 0) where.append(" and ");
      where.append(field.getName()).append(" = ?");
      values.add(value);
    }
    
    return 0 < countWhere(tableName, where.toString(), values.toArray(new Object[values.size()]));
  }
  
  @Override
  public <T> List<T> seleList(Class<T> cl, CharSequence sql, Object... params) {
    return seleList(newInstanciator(cl), sql, params);
  }
  
  @Override
  public <T> List<T> seleList(Creator<T> cl, CharSequence sql, Object... params) {
    List<Object> list = new ArrayList<>(params.length);
    for (Object object : params) {
      list.add(object);
    }
    return selectList(cl, sql, list);
  }
  
  @Override
  public <T> List<T> selectList(Class<T> cl, CharSequence sql, List<Object> params) {
    return selectList(cl, sql, params, 0, 0);
  }
  
  private static <T> Creator<T> newInstanciator(final Class<T> cl) {
    if (cl == Integer.class || cl == Integer.TYPE) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)Integer.valueOf(0);
      }
    };
    if (cl == Long.class || cl == Long.TYPE) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)Long.valueOf(0);
      }
    };
    if (cl == Double.class || cl == Double.TYPE) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)Double.valueOf(0);
      }
    };
    if (cl == Float.class || cl == Float.TYPE) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)Float.valueOf(0);
      }
    };
    if (cl == Boolean.class || cl == Boolean.TYPE) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)Boolean.FALSE;
      }
    };
    if (cl == BigDecimal.class) return new Creator<T>() {
      @SuppressWarnings("unchecked")
      @Override
      public T create() {
        return (T)BigDecimal.ZERO;
      }
    };
    
    return new Creator<T>() {
      @Override
      public T create() {
        try {
          return cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
  
  @Override
  public <T> List<T> selectList(Creator<T> creator, CharSequence sql, List<Object> params) {
    return selectList(creator, sql, params, 0, 0);
  }
  
  @Override
  public <T> List<T> selectList(Class<T> cl, CharSequence sql, List<Object> params, int offset,
      int count) {
    return selectList(newInstanciator(cl), sql, params, offset, count);
  }
  
  @Override
  public <T> List<T> selectList(final Creator<T> creator, final CharSequence sql,
      final List<Object> params, final int offset, final int count) {
    
    return jdbc().execute(new ConnectionCallback<List<T>>() {
      @Override
      public List<T> doInConnection(Connection con) throws SQLException, DataAccessException {
        Result result = new Result();
        result.creator = creator;
        result.sqlViewer = sqlViewer();
        result.type = ResultType.LIST;
        
        SqlWithParams sqlp = SqlWithParams.selectWith(sql.toString(), params);
        sqlp.page(DbTypeDetector.detect(con), offset, count);
        
        return OperUtil.call(con, sqlp, result);
      }
    });
  }
  
  @Override
  public int execUpdate(CharSequence sql, Object... params) {
    List<Object> list = new ArrayList<>();
    for (Object x : params) {
      list.add(x);
    }
    return executeUpdate(sql, list);
  }
  
  @Override
  public int executeUpdate(final CharSequence sql, final List<Object> params) {
    return jdbc().execute(new ConnectionCallback<Integer>() {
      @Override
      public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
        SqlWithParams sqlp = SqlWithParams.modiWith(sql.toString(), params);
        Result result = Result.simple(Integer.class).with(sqlViewer());
        return OperUtil.call(con, sqlp, result);
      }
    });
  }
  
  @Override
  public long seleLong(CharSequence sql, Object... params) {
    List<Object> list = new ArrayList<>();
    for (Object object : params) {
      list.add(object);
    }
    return selectLong(sql, list);
  }
  
  @Override
  public long selectLong(final CharSequence sql, final List<Object> params) {
    return jdbc().execute(new ConnectionCallback<Long>() {
      @Override
      public Long doInConnection(Connection con) throws SQLException, DataAccessException {
        SqlWithParams sqlp = SqlWithParams.selectWith(sql.toString(), params);
        Result result = Result.simple(Long.class).with(sqlViewer());
        return OperUtil.call(con, sqlp, result);
      }
    });
  }
  
  @Override
  public int seleInt(CharSequence sql, Object... params) {
    List<Object> list = new ArrayList<>();
    for (Object object : params) {
      list.add(object);
    }
    return selectInt(sql, list);
  }
  
  @Override
  public int selectInt(final CharSequence sql, final List<Object> params) {
    return jdbc().execute(new ConnectionCallback<Integer>() {
      @Override
      public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
        SqlWithParams sqlp = SqlWithParams.selectWith(sql.toString(), params);
        Result result = Result.simple(Integer.class).with(sqlViewer());
        return OperUtil.call(con, sqlp, result);
      }
    });
  }
  
  public <T> T save(String tableName, boolean insert, T object) {
    if (insert) {
      insert(tableName, object);
    } else {
      update(tableName, object);
    }
    return object;
  };
  
  @Override
  public String getStrField(String tableName, String gettingField, Object... fieldValuePairs) {
    return getField(String.class, tableName, gettingField, fieldValuePairs);
  }
  
  @Override
  public Long getLongField(String tableName, String gettingField, Object... fieldValuePairs) {
    return getField(Long.class, tableName, gettingField, fieldValuePairs);
  }
  
  @Override
  public Integer getIntField(String tableName, String gettingField, Object... fieldValuePairs) {
    return getField(Integer.class, tableName, gettingField, fieldValuePairs);
  }
  
  @Override
  public Date getTimeField(String tableName, String gettingField, Object... fieldValuePairs) {
    return getField(Date.class, tableName, gettingField, fieldValuePairs);
  }
  
  @Override
  public Double getDoubleField(String tableName, String gettingField, Object... fieldValuePairs) {
    return getField(Double.class, tableName, gettingField, fieldValuePairs);
  }
  
  @Override
  public boolean getBoolField(String tableName, String gettingField, Object... fieldValuePairs) {
    return bool(getField(Boolean.class, tableName, gettingField, fieldValuePairs));
  }
  
  public static boolean bool(Boolean obj) {
    if (obj == null) return false;
    return obj.booleanValue();
  }
}
