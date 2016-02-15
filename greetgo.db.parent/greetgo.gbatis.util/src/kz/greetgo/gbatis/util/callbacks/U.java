package kz.greetgo.gbatis.util.callbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kz.greetgo.gbatis.futurecall.SqlViewer;

public class U {
  
  public static boolean need(SqlViewer sqlViewer) {
    if (sqlViewer == null) return false;
    return sqlViewer.needView();
  }
  
  public static void view(long startedAt, SqlViewer sqlViewer, Exception err, Object sql,
      List<Object> params) {
    if (sqlViewer == null) return;
    
    try {
      if (!sqlViewer.needView()) return;
      
      if (sql == null) sql = "sql == null";
      sqlViewer.view(sql.toString(), params, System.currentTimeMillis() - startedAt, err);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  public static void view(long startedAt, SqlViewer sqlViewer, Exception err, Object sql,
      Object[] params) {
    if (sqlViewer == null) return;
    
    try {
      long delay = System.currentTimeMillis() - startedAt;
      if (!sqlViewer.needView()) return;
      List<Object> list = new ArrayList<>();
      for (Object p : params) {
        list.add(p);
      }
      
      if (sql == null) sql = "sql == null";
      sqlViewer.view(sql.toString(), list, delay, err);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  public static final Pattern START_WITH_AND = Pattern.compile("\\s*AND\\s.*",
      Pattern.CASE_INSENSITIVE);
  
  public static void appendWhere(StringBuilder sql, String where) {
    if (where == null) return;
    
    if (START_WITH_AND.matcher(where).matches()) {
      sql.append(" where 1=1 ").append(where);
    } else {
      sql.append(" where ").append(where);
    }
  }
  
  public static final String SQLERROR = "SQLERROR";
}
