package nf36_example_with_depinject.tests;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.db.nf36.core.Nf36WhereUpdater;
import kz.greetgo.db.nf36.core.SqlLogAcceptor;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.beans.postgres.BeanConfigPostgres;
import nf36_example_with_depinject.errors.SqlError;
import nf36_example_with_depinject.jdbc.ByTwo;
import nf36_example_with_depinject.jdbc.CountWhere;
import nf36_example_with_depinject.util.DbTypeSource;
import org.testng.annotations.Test;

import java.sql.PreparedStatement;

import static kz.greetgo.db.nf36.Nf36Builder.newNf36Builder;
import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigPostgres.class)
public class JdbcNf36WhereUpdaterAdapterPostgresTests extends AbstractDepinjectTestNg {

  public BeanGetter<Jdbc> jdbc;
  public BeanGetter<DbTypeSource> dbTypeSource;
  public BeanGetter<SqlLogAcceptor> logAcceptor;

  protected void exec(String sql) {
    jdbc.get().execute((ConnectionCallback<Void>) con -> {
      try (PreparedStatement st = con.prepareStatement(sql)) {
        System.out.println("EXEC SQL " + sql);
        st.executeUpdate();
      }
      return null;
    });
  }

  protected void dropTable(String tableName) {
    try {
      exec("drop table " + tableName);
    } catch (SqlError e) {
      //noinspection StatementWithEmptyBody
      if (e.sqlState.equals("42P01")) {
        //ignore
      } else throw e;
    }
  }

  protected void prepareStructure() {
    dropTable("tmp1_f1");
    dropTable("tmp1_f2");
    dropTable("tmp1");
    createTableTmp1();
    createTableTmp1f1();
    createTableTmp1f2();
  }

  protected void createTableTmp1() {
    exec("create table tmp1 (" +
      "  id1   varchar(32)," +
      "  id2   varchar(32)," +
      "  name1 varchar(100)," +
      "  name2 varchar(100)," +
      "  f1    varchar(100) not null default 'def value'," +
      "  f2    varchar(100) not null default 'def value'," +
      "  primary key(id1, id2)" +
      ")");
  }

  private void createTableTmp1f1() {
    exec("create table tmp1_f1 (" +
      "  id1   varchar(32)," +
      "  id2   varchar(32)," +
      "  ts    timestamp not null default clock_timestamp()," +
      "  f1    varchar(100) not null default 'def value'," +
      "  foreign key (id1, id2) references tmp1(id1, id2)," +
      "  primary key(id1, id2, ts)" +
      ")");
  }

  private void createTableTmp1f2() {
    exec("create table tmp1_f2 (" +
      "  id1   varchar(32)," +
      "  id2   varchar(32)," +
      "  ts    timestamp not null default clock_timestamp()," +
      "  f2    varchar(100)," +
      "  foreign key (id1, id2) references tmp1(id1, id2)," +
      "  primary key(id1, id2, ts)" +
      ")");
  }

  @Test
  public void simpleUpdateWhere() {
    prepareStructure();

    exec("insert into tmp1 (id1, id2, name1, name2) values ('1','1','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('1','2','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('2','1','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('2','2','ok1','ok2')");

    exec("insert into tmp1 (id1, id2, name1, name2) values ('100','100','left','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('101','101','ok1','left')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('103','103','left','left')");

    Nf36WhereUpdater whereUpdater = newNf36Builder()
      .whereUpdater()
      .database(dbTypeSource.get().currentDbType())
      .setJdbc(jdbc.get())
      .setLogAcceptor(logAcceptor.get())
      .build()
      .setAuthor("Сталина на вас нет");

    whereUpdater.setNf3TableName("tmp1");
    whereUpdater.setIdFieldNames("id1", "id2");
    whereUpdater.setField("tmp1_f1", "f1", "value 1");
    whereUpdater.setField("tmp2_f2", "f2", "value 2");
    whereUpdater.where("name1", "ok1");
    whereUpdater.where("name2", "ok2");

    assertTmp1("1", "1", "f1", "def value").assertTmp1("1", "1", "f2", "def value");
    assertTmp1("1", "2", "f1", "def value").assertTmp1("1", "2", "f2", "def value");
    assertTmp1("2", "1", "f1", "def value").assertTmp1("2", "1", "f2", "def value");
    assertTmp1("2", "2", "f1", "def value").assertTmp1("2", "2", "f2", "def value");

    whereUpdater.commit();

    assertTmp1("1", "1", "f1", "value 1").assertTmp1("1", "1", "f2", "value 2");
    assertTmp1("1", "2", "f1", "value 1").assertTmp1("1", "2", "f2", "value 2");
    assertTmp1("2", "1", "f1", "value 1").assertTmp1("2", "1", "f2", "value 2");
    assertTmp1("2", "2", "f1", "value 1").assertTmp1("2", "2", "f2", "value 2");

    assertTmp1("100", "100", "f1", "def value");
    assertTmp1("101", "101", "f1", "def value");
    assertTmp1("102", "102", "f1", "def value");

    assertThat(jdbc.get().execute(new CountWhere("tmp1_f1", null))).isEqualTo(4);
    assertThat(jdbc.get().execute(new CountWhere("tmp1_f2", null))).isEqualTo(4);
  }

  private JdbcNf36WhereUpdaterAdapterPostgresTests assertTmp1(String id1, String id2, String field, String expectedValue) {

    {
      String actualValue = jdbc.get().execute(new ByTwo<>("id1", id1, "id2", id2, "tmp1", field));
      assertThat(actualValue)
        .describedAs("tmp1(.id1 = " + id1 + ", .id2 = " + id1 + ")." + field
          + " == '" + actualValue + "', but expected '" + expectedValue + "'")
        .isEqualTo(expectedValue);
    }

    if (!"def value".equals(expectedValue)) {
      String actualValue = jdbc.get().execute(new ByTwo<>("id1", id1, "id2", id2, "tmp1_" + field, field));
      assertThat(actualValue)
        .describedAs("tmp1_" + field + "(.id1 = " + id1 + ", .id2 = " + id1 + ")." + field
          + " == '" + actualValue + "', but expected '" + expectedValue + "'")
        .isEqualTo(expectedValue);
    }

    return this;
  }
}
