package kz.greetgo.gbatis.util.impl;

import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.util.iface.TableName;
import kz.greetgo.gbatis.util.test.MyTestBase;
import kz.greetgo.util.db.DbType;
import org.testng.annotations.Test;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractUtilRegisterTest2 extends MyTestBase {

  @TableName("test_client")
  public static class Client {
    public String id, surname, name;

    public Client(String id, String surname, String name) {
      this.id = id;
      this.surname = surname;
      this.name = name;
    }
  }

  @Override
  protected DbType[] usingDbTypes() {
    return new DbType[]{DbType.Oracle, DbType.PostgreSQL};
  }

  @Test(dataProvider = CONNECT_PROVIDER)
  public void insertOrUpdate(Connection con) throws Exception {
    TestingUtilRegister r = new TestingUtilRegister();
    r.sqlViewer = new SqlViewer() {
      @Override
      public boolean needView() {
        return false;
      }

      @Override
      public void view(String sql, List<Object> params, long delay, Exception err) {
        System.out.println((err == null ? "" : "ERROR ") + sql);
        System.out.println(params);
      }
    };
    r.setConnection(con);

    queryForce(con, "drop table test_client");
    query(con, "create table test_client(id varchar(20) not null primary key," +
        " surname varchar(20), name varchar(20), age1 int, age2 int)");

    query(con, "insert into test_client values ('cl_01', 'Down', 'Up', 100, 200)");

    queryForce(con, "drop table test_client2");
    query(con, "create table test_client2(id varchar(20) not null primary key," +
        " surname varchar(20), name varchar(20), age1 int, age2 int)");

    query(con, "insert into test_client2 values ('cl_01', 'Down', 'Up', 100, 200)");

    queryForce(con, "drop table test_client3");
    query(con, "create table test_client3 (" +
        " id varchar(20) not null," +
        " surname varchar(20) not null," +
        " name varchar(20)," +
        " age1 int," +
        " age2 int," +
        " primary key (id, surname)" +
        ")");

    query(con, "insert into test_client3 values ('cl_01', 'Down', 'Up', 100, 200)");

    queryForce(con, "drop table test_client4");
    query(con, "create table test_client4 (" +
        " id varchar(20) not null," +
        " surname varchar(20) not null," +
        " primary key (id, surname)" +
        ")");

    //
    //
    InsertOrUpdate iou = r.insertOrUpdate();

    iou.object(new Client("cl_01", "Stone", "Sun"), "age1", 333);
    iou.object(new Client("cl_02", "Boom", "Line"), "age1", 444, "age2", 800);

    iou.objectTable(new Client("cl_01", "Rock", "Mars"), "test_client2", "age1", 333);
    iou.objectTable(new Client("cl_02", "Moon", "Malina"), "test_client2", "age1", 444, "age2", 800);

    iou.objectTable(new Client("cl_01", "Down", "Mars"), "test_client3", "age1", 777);
    iou.objectTable(new Client("cl_02", "Sun", "Venus"), "test_client3", "age1", 234, "age2", 876);

    iou.objectTable(new Client("cl_01", "Sun", "Venus"), "test_client4");
    iou.objectTable(new Client("cl_01", "Sun", "Venus"), "test_client4");

    iou.go();
    //
    //

    assertThat(r.getStrField("test_client", "surname", "id", "cl_01")).isEqualTo("Stone");
    assertThat(r.getStrField("test_client", "surname", "id", "cl_02")).isEqualTo("Boom");
    assertThat(r.getIntField("test_client", "age1", "id", "cl_01")).isEqualTo(333);
    assertThat(r.getIntField("test_client", "age1", "id", "cl_02")).isEqualTo(444);

    assertThat(r.getStrField("test_client2", "surname", "id", "cl_01")).isEqualTo("Rock");
    assertThat(r.getStrField("test_client2", "surname", "id", "cl_02")).isEqualTo("Moon");

    assertThat(r.getStrField("test_client4", "surname", "id", "cl_01")).isEqualTo("Sun");
  }

  @Test(dataProvider = CONNECT_PROVIDER, enabled = false)
  public void testSql(Connection con) throws Exception {
    queryForce(con, "drop table asd");
    query(con, "create table asd(asd varchar(1000))");

    queryForce(con, "drop table test_client");
    query(con, "create table test_client(id varchar(20) not null primary key," +
        " surname varchar(20), name varchar(20), age1 int, age2 int)");

    query(con, "insert into test_client values ('cl_01', 'Down', 'Up', 100, 200)");

    StringBuilder sql = new StringBuilder();
    sql.append("merge /*+ parallel(des, 4) */ into test_client des using (\n");
    sql.append(" select ? as id, ? as surname, ? as name from dual\n");
    sql.append(") src ON (des.id = src.id)\n");
    sql.append("when matched then update\n");
    sql.append("  set des.surname = src.surname\n");
    sql.append("  ,   des.name    = src.name\n");
    sql.append("when not matched then insert (\n");
    sql.append("  des.id, des.surname, des.name\n");
    sql.append(") values (\n");
    sql.append("  src.id, src.surname, src.name\n");
    sql.append(")\n");

    try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
      ps.setString(1, "cl_01");
      ps.setString(2, "good surname 1");
      ps.setString(3, "good name 1");
      ps.addBatch();

      ps.setString(1, "cl_02");
      ps.setString(2, "good surname 2");
      ps.setString(3, "good name 2");
      ps.addBatch();

      ps.executeBatch();

    } catch (BatchUpdateException e) {
      throw e.getNextException();
    }
  }
}
