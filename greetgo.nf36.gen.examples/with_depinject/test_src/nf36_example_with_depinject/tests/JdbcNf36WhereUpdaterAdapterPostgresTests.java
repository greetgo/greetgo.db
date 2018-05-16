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
import nf36_example_with_depinject.jdbc.ByOne;
import nf36_example_with_depinject.jdbc.ByOneLast;
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
      if (e.type == SqlError.Type.DROP_TABLE) {
        //ignore
      } else throw e;
    }
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

  protected void createTableTmp1f1() {
    exec("create table tmp1_f1 (" +
      "  id1   varchar(32)," +
      "  id2   varchar(32)," +
      "  ts    timestamp not null default clock_timestamp()," +
      "  f1    varchar(100) not null default 'def value'," +
      "  foreign key (id1, id2) references tmp1(id1, id2)," +
      "  primary key(id1, id2, ts)" +
      ")");
  }

  protected void createTableTmp1f2() {
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
  public void multipleIdUpdateWhere() {
    dropTable("tmp1_f1");
    dropTable("tmp1_f2");
    dropTable("tmp1");
    createTableTmp1();
    createTableTmp1f1();
    createTableTmp1f2();

    exec("insert into tmp1 (id1, id2, name1, name2) values ('1','1','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('1','2','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('2','1','ok1','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('2','2','ok1','ok2')");

    exec("insert into tmp1 (id1, id2, name1, name2) values ('100','100','left','ok2')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('101','101','ok1','left')");
    exec("insert into tmp1 (id1, id2, name1, name2) values ('102','102','left','left')");

    Nf36WhereUpdater whereUpdater = createUpdater()
      //.setAuthor("Сталина на вас нет")
      ;

    whereUpdater.setNf3TableName("tmp1");
    whereUpdater.setIdFieldNames("id1", "id2");
    whereUpdater.setField("tmp1_f1", "f1", "value 1");
    whereUpdater.setField("tmp1_f2", "f2", "value 2");
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

  private String readByOne(String idField, String idValue, String tableName, String fieldName) {
    return jdbc.get().execute(new ByOne<>(idField, idValue, tableName, fieldName));
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

  private String readLastByOne(String idName, String idValue, String tableName, String fieldName) {
    return jdbc.get().execute(new ByOneLast<>(idName, idValue, tableName, fieldName));
  }

  private Nf36WhereUpdater createUpdater() {
    return newNf36Builder()
      .whereUpdater()
      .database(dbTypeSource.get().currentDbType())
      .setJdbc(jdbc.get())
      .setLogAcceptor(logAcceptor.get())
      .build();
  }

  protected void createTableAdam() {
    exec("create table adam (" +
      "  id varchar(32)," +
      "  surname varchar(300)," +
      "  name varchar(300)," +
      "  patronymic varchar(300)," +
      "  primary key(id)" +
      ")");
  }

  protected void createTableAdamSurname() {
    exec("create table adam_surname (" +
      "  id varchar(32)," +
      "  ts timestamp default clock_timestamp()," +
      "  surname varchar(300)," +
      "  foreign key (id) references adam(id)," +
      "  primary key(id, ts)" +
      ")");
  }

  protected void createTableAdamName() {
    exec("create table adam_name (" +
      "  id varchar(32)," +
      "  ts timestamp default clock_timestamp()," +
      "  name varchar(300)," +
      "  foreign key (id) references adam(id)," +
      "  primary key(id, ts)" +
      ")");
  }

  protected void createTableAdamPatronymic() {
    exec("create table adam_Patronymic (" +
      "  id varchar(32)," +
      "  ts timestamp default clock_timestamp()," +
      "  Patronymic varchar(300)," +
      "  foreign key (id) references adam(id)," +
      "  primary key(id, ts)" +
      ")");
  }

  @Test
  public void simpleUpdate() {
    dropTable("adam_surname");
    dropTable("adam_name");
    dropTable("adam_patronymic");
    dropTable("adam");
    createTableAdam();
    createTableAdamSurname();
    createTableAdamName();
    createTableAdamPatronymic();

    String ins = "insert into adam (id,surname,name,patronymic)values";
    exec(ins + "('1','Иванов'  , 'Иван'      , 'Петрович' )");
    exec(ins + "('2','Сидоров' , 'Сидор'     , 'Петрович' )");
    exec(ins + "('3','Пушкин'  , 'Александр' , 'Сергеевич')");
    exec(ins + "('4','Панфилов', 'Георгий'   , 'Петрович' )");

    Nf36WhereUpdater whereUpdater1 = createUpdater();

    whereUpdater1.setNf3TableName("adam");
    whereUpdater1.setIdFieldNames("id");
    whereUpdater1.setField("adam_name", "name", "Пётр");
    whereUpdater1.where("patronymic", "Петрович");

    assertThat(readByOne("id", "1", "adam", "name")).isEqualTo("Иван");
    assertThat(readByOne("id", "2", "adam", "name")).isEqualTo("Сидор");
    assertThat(readByOne("id", "3", "adam", "name")).isEqualTo("Александр");
    assertThat(readByOne("id", "4", "adam", "name")).isEqualTo("Георгий");

    whereUpdater1.commit();

    assertThat(readByOne("id", "1", "adam", "name")).isEqualTo("Пётр");
    assertThat(readByOne("id", "2", "adam", "name")).isEqualTo("Пётр");
    assertThat(readByOne("id", "3", "adam", "name")).isEqualTo("Александр");
    assertThat(readByOne("id", "4", "adam", "name")).isEqualTo("Пётр");

    assertThat(readLastByOne("id", "1", "adam_name", "name")).isEqualTo("Пётр");
    assertThat(readLastByOne("id", "2", "adam_name", "name")).isEqualTo("Пётр");
    assertThat(readLastByOne("id", "4", "adam_name", "name")).isEqualTo("Пётр");

    Nf36WhereUpdater whereUpdater2 = createUpdater();

    whereUpdater2.setNf3TableName("adam");
    whereUpdater2.setIdFieldNames("id");
    whereUpdater2.setField("adam_name", "name", "Егор");
    whereUpdater2.where("patronymic", "Петрович");

    whereUpdater2.commit();

    assertThat(readByOne("id", "1", "adam", "name")).isEqualTo("Егор");
    assertThat(readByOne("id", "2", "adam", "name")).isEqualTo("Егор");
    assertThat(readByOne("id", "3", "adam", "name")).isEqualTo("Александр");
    assertThat(readByOne("id", "4", "adam", "name")).isEqualTo("Егор");

    assertThat(readLastByOne("id", "1", "adam_name", "name")).isEqualTo("Егор");
    assertThat(readLastByOne("id", "2", "adam_name", "name")).isEqualTo("Егор");
    assertThat(readLastByOne("id", "4", "adam_name", "name")).isEqualTo("Егор");
  }
}
