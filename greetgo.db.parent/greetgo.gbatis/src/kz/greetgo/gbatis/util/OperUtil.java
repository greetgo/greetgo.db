package kz.greetgo.gbatis.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Result;
import kz.greetgo.gbatis.model.SqlWithParams;

public class OperUtil {
  
  public static boolean hasColumn(ResultSet rs, String name, Map<String, Boolean> hasColumnCache)
      throws SQLException {
    if (hasColumnCache != null) {
      Boolean ret = hasColumnCache.get(name);
      if (ret != null) return ret;
    }
    try {
      rs.findColumn(name);
      if (hasColumnCache != null) hasColumnCache.put(name, true);
      return true;
    } catch (SQLException e) {
      if (hasColumnCache != null) hasColumnCache.put(name, false);
      return false;
    }
  }
  
  public static String toUnderScore(String str) {
    StringBuilder sb = new StringBuilder();
    boolean wasLowercased = false;
    for (int i = 0, C = str.length(); i < C; i++) {
      char c = str.charAt(i);
      
      if (Character.isLowerCase(c)) {
        wasLowercased = true;
        sb.append(c);
        continue;
      }
      if (Character.isUpperCase(c) && wasLowercased) {
        sb.append('_');
        sb.append(Character.toLowerCase(c));
        wasLowercased = false;
        continue;
      }
      wasLowercased = false;
      
      sb.append(c);
    }
    return sb.toString();
  }
  
  public static void copyRow(ResultSet rs, Object object, Map<String, Boolean> hasColumnCache)
      throws SQLException, IllegalAccessException {
    for (Setter setter : ReflectUtil.scanSetters(object).values()) {
      
      {
        String name = toUnderScore(setter.name());
        if (hasColumn(rs, name, hasColumnCache)) {
          setter.set(SqlUtil.fromSql(rs.getObject(name), setter.type()));
          continue;
        }
      }
      
      {
        String name = setter.name();
        if (hasColumn(rs, name, hasColumnCache)) {
          setter.set(SqlUtil.fromSql(rs.getObject(name), setter.type()));
          continue;
        }
      }
      
    }
  }
  
  public static final Set<Class<?>> SIMPLE_CLASSES;
  static {
    Set<Class<?>> x = new HashSet<>();
    
    x.add(Boolean.class);
    x.add(Integer.class);
    x.add(Character.class);
    x.add(Byte.class);
    x.add(Short.class);
    x.add(Double.class);
    x.add(Long.class);
    x.add(Float.class);
    x.add(Double.class);
    x.add(BigDecimal.class);
    x.add(BigInteger.class);
    
    SIMPLE_CLASSES = Collections.unmodifiableSet(x);
  }
  
  public static boolean isSimpleClass(Class<?> cl) {
    if (cl.isPrimitive()) return true;
    if (cl.isEnum()) return true;
    if (cl.isAssignableFrom(String.class)) return true;
    //noinspection SimplifiableIfStatement
    if (cl.isAssignableFrom(Date.class)) return true;
    return SIMPLE_CLASSES.contains(cl);
  }
  
  public static Object createObject(ResultSet rs, Map<String, Boolean> hasColumnCache,
      Result result, int columnOnSimpleType) throws Exception {
    if (result.resultDataClass != null && isSimpleClass(result.resultDataClass)) {
      return SqlUtil.fromSql(rs.getObject(columnOnSimpleType), result.resultDataClass);
    }
    
    {
      //noinspection ConstantConditions
      Object object = result.creator == null ? //
      result.resultDataClass.newInstance() //
          :result.creator.create();
      copyRow(rs, object, hasColumnCache);
      return object;
    }
  }
  
  private static <T> T callModi(Connection con, SqlWithParams sql, Result result) throws Exception {
    long startedAt = System.currentTimeMillis();
    
    try {

      try (PreparedStatement ps = con.prepareStatement(sql.sql)) {

        {
          int index = 1;
          for (Object param : sql.params) {
            ps.setObject(index++, param);
          }
        }

        T ret = castInt(ps.executeUpdate(), result.resultDataClass);

        view(result.sqlViewer, null, sql, startedAt);

        return ret;

      }
      
    } catch (Exception e) {
      view(result.sqlViewer, e, sql, startedAt);
      throw e;
    }
  }
  
  private static void view(SqlViewer sqlViewer, Exception err, SqlWithParams sql, long startedAt) {
    if (sqlViewer == null) return;
    
    try {
      long delay = System.currentTimeMillis() - startedAt;
      if (!sqlViewer.needView()) return;
      sqlViewer.view(sql.sql, sql.params, delay, err);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T castInt(int value, Class<?> returnClass) {
    if (Integer.class.equals(returnClass) || Integer.TYPE.equals(returnClass)) {
      return (T)Integer.valueOf(value);
    }
    if (Long.class.equals(returnClass) || Long.TYPE.equals(returnClass)) {
      return (T)Long.valueOf(value);
    }
    if (Void.class.equals(returnClass) || Void.TYPE.equals(returnClass)) {
      return null;
    }
    throw new IllegalArgumentException("Cannot use type " + returnClass + " in Modi");
  }
  
  private static <T> T callSelect(Connection con, SqlWithParams sql, Result result)
      throws Exception {
    long startedAt = System.currentTimeMillis();
    
    try {

      try (PreparedStatement ps = con.prepareStatement(sql.sql)) {

        {
          int index = 1;
          for (Object param : sql.params) {
            ps.setObject(index++, param);
          }
        }

        try (ResultSet rs = ps.executeQuery()) {

          T ret = assemble(rs, result);

          view(result.sqlViewer, null, sql, startedAt);

          return ret;

        }

      }
      
    } catch (Exception e) {
      view(result.sqlViewer, e, sql, startedAt);
      throw e;
    }
  }
  
  private static <T> T callFunction(Connection con, SqlWithParams sql, Result result)
      throws Exception {
    long startedAt = System.currentTimeMillis();
    
    try {

      try (CallableStatement cs = con.prepareCall(sql.sql)) {

        {
          int index = 1;
          for (Object param : sql.params) {
            cs.setObject(index++, param);
          }
        }

        cs.execute();

        view(result.sqlViewer, null, sql, startedAt);

      }
      
      return null;
      
    } catch (Exception e) {
      view(result.sqlViewer, e, sql, startedAt);
      throw e;
    }
  }
  
  private static <T> T assemble(ResultSet rs, Result result) throws Exception {
    
    switch (result.type) {
    case SIMPLE:
      return assembleSimple(rs, result);
      
    case LIST:
      return assembleList(rs, result);
      
    case SET:
      return assembleSet(rs, result);
      
    case MAP:
      return assembleMap(rs, result);
      
    case MAP_LIST:
      return assembleMapList(rs, result);
      
    case MAP_SET:
      return assembleMapSet(rs, result);
      
    default:
      throw new IllegalArgumentException("Unknown request.resultType = " + result.type);
    }
    
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleMapSet(ResultSet rs, Result result) throws Exception {
    Map<String, Boolean> hasColumnCache = new HashMap<>();
    
    Map<Object, Set<Object>> ret = new HashMap<>();
    
    while (rs.next()) {
      Object[] keyValue = extractKeyValue(rs, result, hasColumnCache);
      
      Object key = keyValue[0];
      
      Set<Object> list = ret.get(key);
      if (list == null) {
        ret.put(key, list = new HashSet<>());
      }
      
      list.add(keyValue[1]);
    }
    
    return (T)ret;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleMapList(ResultSet rs, Result result) throws Exception {
    Map<String, Boolean> hasColumnCache = new HashMap<>();
    
    Map<Object, List<Object>> ret = new HashMap<>();
    
    while (rs.next()) {
      Object[] keyValue = extractKeyValue(rs, result, hasColumnCache);
      
      Object key = keyValue[0];
      
      List<Object> list = ret.get(key);
      if (list == null) {
        ret.put(key, list = new ArrayList<>());
      }
      
      list.add(keyValue[1]);
    }
    
    return (T)ret;
  }
  
  private static Object[] extractKeyValue(ResultSet rs, Result result,
      Map<String, Boolean> hasColumnCache) throws Exception {
    Object object = createObject(rs, hasColumnCache, result, result.mapKeyField == null ? 2 :1);
    Object keyValue = result.mapKeyField == null ? rs.getObject(1) :rs
        .getObject(result.mapKeyField);
    Object key = SqlUtil.fromSql(keyValue, result.mapKeyClass);
    
    return new Object[] { key, object };
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleMap(ResultSet rs, Result result) throws Exception {
    Map<Object, Object> ret = new HashMap<>();
    Map<String, Boolean> hasColumnCache = new HashMap<>();
    while (rs.next()) {
      
      Object[] keyValue = extractKeyValue(rs, result, hasColumnCache);
      
      ret.put(keyValue[0], keyValue[1]);
      
    }
    return (T)ret;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleList(ResultSet rs, Result result) throws Exception {
    List<Object> ret = new ArrayList<>();
    Map<String, Boolean> hasColumnCache = new HashMap<>();
    while (rs.next()) {
      ret.add(createObject(rs, hasColumnCache, result, 1));
    }
    return (T)ret;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleSet(ResultSet rs, Result result) throws Exception {
    Set<Object> ret = new HashSet<>();
    Map<String, Boolean> columnCache = new HashMap<>();
    while (rs.next()) {
      ret.add(createObject(rs, columnCache, result, 1));
    }
    return (T)ret;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T assembleSimple(ResultSet rs, Result result) throws Exception {
    if (!rs.next()) {
      if (Boolean.class.equals(result.resultDataClass)) return (T)Boolean.FALSE;
      if (Boolean.TYPE.equals(result.resultDataClass)) return (T)Boolean.FALSE;
      return null;
    }
    return (T)createObject(rs, null, result, 1);
  }
  
  public static <T> T callException(Connection con, SqlWithParams sql, Result result)
      throws Exception {
    switch (sql.type) {
    case Call:
      return callFunction(con, sql, result);
      
    case Sele:
      return callSelect(con, sql, result);
      
    case Modi:
      return callModi(con, sql, result);
      
    default:
      throw new IllegalArgumentException("Unknown request type = " + sql.type);
    }
  }
  
  public static <T> T call(Connection con, SqlWithParams sql, Result result) {
    try {
      return callException(con, sql, result);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
