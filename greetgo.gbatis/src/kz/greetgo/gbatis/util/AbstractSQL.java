package kz.greetgo.gbatis.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({"unused", "UnusedReturnValue"})
abstract class AbstractSQL<T> {
  
  private static final String AND = ") \nAND (";
  private static final String OR = ") \nOR (";
  
  public abstract T getSelf();
  
  protected abstract T createNew();
  
  private final Map<String, T> withMap = new LinkedHashMap<>();
  
  public T WITH(String view) {
    if (withMap.containsKey(view)) {
      throw new IllegalArgumentException("Already exists " + view);
    }
    
    {
      T t = createNew();
      
      withMap.put(view, t);
      
      return t;
    }
  }
  
  public T UPDATE(String table) {
    sql().statementType = SQLStatement.StatementType.UPDATE;
    sql().tables.add(table);
    return getSelf();
  }
  
  public T SET(String sets) {
    sql().sets.add(sets);
    return getSelf();
  }
  
  public T INSERT_INTO(String tableName) {
    sql().statementType = SQLStatement.StatementType.INSERT;
    sql().tables.add(tableName);
    return getSelf();
  }
  
  public T VALUES(String columns, String values) {
    sql().columns.add(columns);
    sql().values.add(values);
    return getSelf();
  }
  
  public T SELECT(String columns) {
    sql().statementType = SQLStatement.StatementType.SELECT;
    sql().select.add(columns);
    return getSelf();
  }
  
  public T SELECT_DISTINCT(String columns) {
    sql().distinct = true;
    SELECT(columns);
    return getSelf();
  }
  
  public T DELETE_FROM(String table) {
    sql().statementType = SQLStatement.StatementType.DELETE;
    sql().tables.add(table);
    return getSelf();
  }
  
  public T FROM(String table) {
    sql().tables.add(table);
    return getSelf();
  }
  
  public T JOIN(String join) {
    sql().join.add(join);
    return getSelf();
  }
  
  public T INNERJOIN(String join) {
    sql().innerJoin.add(join);
    return getSelf();
  }
  
  public T LEFTJOIN(String join) {
    sql().leftOuterJoin.add(join);
    return getSelf();
  }
  
  public T RIGHTJOIN(String join) {
    sql().rightOuterJoin.add(join);
    return getSelf();
  }
  
  public T OUTERJOIN(String join) {
    sql().outerJoin.add(join);
    return getSelf();
  }
  
  public T WHERE(String conditions) {
    sql().where.add(conditions);
    sql().lastList = sql().where;
    return getSelf();
  }
  
  public T OR() {
    sql().lastList.add(OR);
    return getSelf();
  }
  
  public T AND() {
    sql().lastList.add(AND);
    return getSelf();
  }
  
  public T GROUP_BY(String columns) {
    sql().groupBy.add(columns);
    return getSelf();
  }
  
  public T HAVING(String conditions) {
    sql().having.add(conditions);
    sql().lastList = sql().having;
    return getSelf();
  }
  
  public T ORDER_BY(String columns) {
    sql().orderBy.add(columns);
    return getSelf();
  }
  
  private SQLStatement sql = new SQLStatement();
  
  private SQLStatement sql() {
    return sql;
  }
  
  public <A extends Appendable> A usingAppender(A a) {
    sql().sql(a);
    return a;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    if (withMap.size() > 0) {
      sb.append("with ");
      
      boolean needComma = false;
      
      for (Entry<String, T> e : withMap.entrySet()) {
        String name = e.getKey();
        String sql = e.getValue().toString();
        
        if (needComma) sb.append("\n, ");
        needComma = true;
        
        sb.append(name).append(" as (");
        sb.append(sql).append(") ");
      }
      
      sb.append('\n');
      sb.append('\n');
    }
    
    sql().sql(sb);
    
    return sb.toString();
  }
  
  private static class SafeAppendable {
    private final Appendable a;
    private boolean empty = true;
    
    public SafeAppendable(Appendable a) {
      super();
      this.a = a;
    }
    
    public SafeAppendable append(CharSequence s) {
      try {
        if (empty && s.length() > 0) empty = false;
        a.append(s);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return this;
    }
    
    public boolean isEmpty() {
      return empty;
    }
    
  }
  
  private static class SQLStatement {
    
    public enum StatementType {
      DELETE, INSERT, SELECT, UPDATE
    }
    
    StatementType statementType;
    List<String> sets = new ArrayList<>();
    List<String> select = new ArrayList<>();
    List<String> tables = new ArrayList<>();
    List<String> join = new ArrayList<>();
    List<String> innerJoin = new ArrayList<>();
    List<String> outerJoin = new ArrayList<>();
    List<String> leftOuterJoin = new ArrayList<>();
    List<String> rightOuterJoin = new ArrayList<>();
    List<String> where = new ArrayList<>();
    List<String> having = new ArrayList<>();
    List<String> groupBy = new ArrayList<>();
    List<String> orderBy = new ArrayList<>();
    List<String> lastList = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    List<String> values = new ArrayList<>();
    boolean distinct;
    
    private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open,
        String close, String conjunction) {
      if (!parts.isEmpty()) {
        if (!builder.isEmpty()) builder.append("\n");
        builder.append(keyword);
        builder.append(" ");
        builder.append(open);
        String last = "________";
        for (int i = 0, n = parts.size(); i < n; i++) {
          String part = parts.get(i);
          if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND)
              && !last.equals(OR)) {
            builder.append(conjunction);
          }
          builder.append(part);
          last = part;
        }
        builder.append(close);
      }
    }
    
    private String selectSQL(SafeAppendable builder) {
      if (distinct) {
        sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
      } else {
        sqlClause(builder, "SELECT", select, "", "", ", ");
      }
      
      sqlClause(builder, "FROM", tables, "", "", ", ");
      sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
      sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
      sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
      sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
      sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
      sqlClause(builder, "WHERE", where, "(", ")", " AND ");
      sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
      sqlClause(builder, "HAVING", having, "(", ")", " AND ");
      sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
      return builder.toString();
    }
    
    private String insertSQL(SafeAppendable builder) {
      sqlClause(builder, "INSERT INTO", tables, "", "", "");
      sqlClause(builder, "", columns, "(", ")", ", ");
      sqlClause(builder, "VALUES", values, "(", ")", ", ");
      return builder.toString();
    }
    
    private String deleteSQL(SafeAppendable builder) {
      sqlClause(builder, "DELETE FROM", tables, "", "", "");
      sqlClause(builder, "WHERE", where, "(", ")", " AND ");
      return builder.toString();
    }
    
    private String updateSQL(SafeAppendable builder) {
      
      sqlClause(builder, "UPDATE", tables, "", "", "");
      sqlClause(builder, "SET", sets, "", "", ", ");
      sqlClause(builder, "WHERE", where, "(", ")", " AND ");
      return builder.toString();
    }
    
    public String sql(Appendable a) {
      SafeAppendable builder = new SafeAppendable(a);
      
      if (statementType == null) {
        return null;
      }
      
      String answer;
      
      switch (statementType) {
      case DELETE:
        answer = deleteSQL(builder);
        break;
      
      case INSERT:
        answer = insertSQL(builder);
        break;
      
      case SELECT:
        answer = selectSQL(builder);
        break;
      
      case UPDATE:
        answer = updateSQL(builder);
        break;
      
      default:
        answer = null;
      }
      
      return answer;
    }
  }
}