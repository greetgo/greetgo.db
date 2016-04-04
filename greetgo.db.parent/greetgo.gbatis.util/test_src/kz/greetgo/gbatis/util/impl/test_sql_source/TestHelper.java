package kz.greetgo.gbatis.util.impl.test_sql_source;

import kz.greetgo.gbatis.util.impl.test_sql_source.oracle.SqlSrcTestOracle;
import kz.greetgo.gbatis.util.impl.test_sql_source.postgres.SqlSrcTestPostgres;
import kz.greetgo.gbatis.util.sqls.SqlSrc;
import kz.greetgo.util.db.DbType;
import kz.greetgo.util.db.DbTypeDetector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

  private final Connection con;

  public TestHelper(Connection con) {
    this.con = con;
  }

  private SqlSrc getTestSqls() throws SQLException {
    DbType dbType = DbTypeDetector.detect(con);
    if (dbType == DbType.PostgreSQL) return SqlSrcTestPostgres.INSTANCE;
    if (dbType == DbType.Oracle) return SqlSrcTestOracle.INSTANCE;
    throw new IllegalArgumentException("Unknown dbType = " + dbType);
  }

  public void executeSql(String sql) throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.execute();
    }
  }

  public String sql(String groupAndId) throws SQLException {
    return getTestSqls().sql(groupAndId);
  }

  public List<String> readTempStorage() throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(sql("listExecutor/readTempStorage"))) {
      try (ResultSet rs = ps.executeQuery()) {
        List<String> ret = new ArrayList<>();
        while (rs.next()) {
          ret.add(rs.getString(1));
        }
        return ret;
      }
    }
  }

  private static boolean ignore(Exception e) {
    return "ERROR: relation \"temp_storage\" already exists".equals(e.getMessage());
  }

  public void prepareTempStorage() throws SQLException {
    try {
      //executeSql(sql("listExecutor/createTempStorage"));
    } catch (Exception e) {
      if (ignore(e)) return;
      throw e;
    }

    executeSql(sql("listExecutor/clear_temp_storage"));
  }

  public void executeCommandList(String commands) throws SQLException {

    try (CallableStatement cs = con.prepareCall("{call q_executeCommandList(?)}")) {
      cs.setString(1, commands);
      cs.execute();
    }

  }

  public void prepareProcedure_sayHelloWorld() throws SQLException {
    executeSql(sql("listExecutor/procedure_sayHelloWorld"));
  }

  public void prepareProcedure_sayQuWithStrStr() throws SQLException {
    executeSql(sql("listExecutor/procedure_sayQuWithStrStr"));
  }

  public void prepareProcedure_sayQuWithIntStr() throws SQLException {
    executeSql(sql("listExecutor/procedure_sayQuWithIntStr"));
  }

  public void prepareProcedure_sayQuWithLongLong() throws SQLException {
    executeSql(sql("listExecutor/procedure_sayQuWithLongLong"));
  }

  public void prepareProcedure_sayQuWithTimeTime() throws SQLException {
    executeSql(sql("listExecutor/procedure_sayQuWithTimeTime"));
  }

  public void executeSqls(String sqls) throws SQLException {
    for (String sql : sqls.split(";;")) {
      executeSql(sql);
    }
  }
}
