package kz.greetgo.gbatis.util.sqls;

import kz.greetgo.gbatis.util.sqls.oracle.SqlSrcOracle;
import kz.greetgo.gbatis.util.sqls.postgres.SqlSrcPostgres;
import kz.greetgo.db.DbType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SqlSrc {
  protected SqlSrc() {
  }

  private static final Map<DbType, SqlSrc> SQL_SRC_MAP = new HashMap<>();

  public static SqlSrc get(Connection con) throws SQLException {
    return get(DbType.detect(con));
  }

  public static SqlSrc get(DbType dbType) {
    {
      SqlSrc ret = SQL_SRC_MAP.get(dbType);
      if (ret != null) return ret;
    }

    synchronized (SQL_SRC_MAP) {
      {
        SqlSrc ret = SQL_SRC_MAP.get(dbType);
        if (ret != null) return ret;
      }
      {
        SqlSrc ret = createNew(dbType);
        SQL_SRC_MAP.put(dbType, ret);
        return ret;
      }
    }
  }

  private static final Map<DbType, SqlSrc> defaultDbTypeMap;

  static {
    Map<DbType, SqlSrc> map = new HashMap<>();
    map.put(DbType.Postgres, new SqlSrcPostgres());
    map.put(DbType.Oracle, new SqlSrcOracle());
    defaultDbTypeMap = Collections.unmodifiableMap(map);
  }

  private static SqlSrc createNew(DbType dbType) {
    SqlSrc sqlSrc = defaultDbTypeMap.get(dbType);
    if (sqlSrc != null) return sqlSrc;
    throw new IllegalArgumentException("dbType = " + dbType);
  }

  private final Map<String, Map<String, String>> groupMaps = new HashMap<>();

  private Map<String, String> getGroupMap(String group) {
    {
      Map<String, String> ret = groupMaps.get(group);
      if (ret != null) return ret;
    }

    synchronized (groupMaps) {
      {
        Map<String, String> ret = groupMaps.get(group);
        if (ret != null) return ret;
      }
      {
        Map<String, String> ret = loadGroupMap(group);
        groupMaps.put(group, ret);
        return ret;
      }
    }
  }

  private Map<String, String> loadGroupMap(String group) {
    try (InputStream in = getClass().getResourceAsStream(group + ".xml")) {

      if (in == null) throw new IllegalArgumentException("Нет такой группы " + group
          + " для класса " + getClass());

      {
        GroupContentHandler gch = new GroupContentHandler();
        gch.readFrom(in);
        return gch.groupContent;
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String sql(String groupAndId) {
    String[] split = groupAndId.split("/");
    if (split.length != 2) throw new IllegalArgumentException("groupAndId должна иметь формат"
        + " = группа/ИД");

    return getGroupMap(split[0]).get(split[1]);
  }
}
