package nf36_example_with_depinject.tests;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.UpserterCreator;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static nf36_example_with_depinject.errors.SqlError.Type.DROP_TABLE;
import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class JdbcNf36UpserterPostgresTest extends ParentDbTests {

  public BeanGetter<UpserterCreator> upserterCreator;

  protected void createTablesForTestFields() {
    exec("create table test_client (" +
        "  id varchar(32) not null," +
        "  surname varchar(300)," +
        "  name varchar(300)," +
        "  birth timestamp," +
        "  age int," +
        "  amount bigint," +
        "" +
        "  primary key(id)" +
        ")");

    exec("create table test_m_client_surname (" +
        "  id varchar(32) not null references test_client," +
        "  ts timestamp default current_timestamp not null," +
        "  surname varchar(300)," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table test_m_client_name (" +
        "  id varchar(32) not null references test_client," +
        "  ts timestamp default current_timestamp not null," +
        "  name varchar(300)," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table test_m_client_birth (" +
        "  id varchar(32) not null references test_client," +
        "  ts timestamp default current_timestamp not null," +
        "  birth timestamp," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table test_m_client_age (" +
        "  id varchar(32) not null references test_client," +
        "  ts timestamp default current_timestamp not null," +
        "  age int," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table test_m_client_amount (" +
        "  id varchar(32) not null references test_client," +
        "  ts timestamp default current_timestamp not null," +
        "  amount bigint," +
        "" +
        "  primary key(id, ts)" +
        ")");
  }

  @Test
  public void insert_update_fields_with_different_types() {

    exec("drop table test_m_client_surname", DROP_TABLE);
    exec("drop table test_m_client_name", DROP_TABLE);
    exec("drop table test_m_client_birth", DROP_TABLE);
    exec("drop table test_m_client_age", DROP_TABLE);
    exec("drop table test_m_client_amount", DROP_TABLE);
    exec("drop table test_client", DROP_TABLE);
    createTablesForTestFields();

    String clientId = "ivan";

    {
      Nf36Upserter upserter = upserterCreator.get().create();

      upserter.setTimeFieldName("ts");
      upserter.setNf3TableName("test_client");

      upserter.putId("id", clientId);
      upserter.putField("test_m_client_surname", "surname", "Иванов");
      upserter.putField("test_m_client_name", "name", "Иван");
      upserter.putField("test_m_client_birth", "birth", ts("2000-01-23 17:11:34"));
      upserter.putField("test_m_client_age", "age", 231786);
      upserter.putField("test_m_client_amount", "amount", 32847895862L);

      upserter.commit();
    }

    assertThat(loadStr("id", clientId, "test_client", "surname")).isEqualTo("Иванов");
    assertThat(loadStr("id", clientId, "test_client", "name")).isEqualTo("Иван");
    assertThat(loadTS("id", clientId, "test_client", "birth")).isEqualTo("2000-01-23 17:11:34");
    assertThat(loadInt("id", clientId, "test_client", "age")).isEqualTo(231786);
    assertThat(loadLong("id", clientId, "test_client", "amount")).isEqualTo(32847895862L);

    assertThat(loadCount("id", clientId, "test_m_client_surname")).isEqualTo(1);
    assertThat(loadCount("id", clientId, "test_m_client_name")).isEqualTo(1);
    assertThat(loadCount("id", clientId, "test_m_client_birth")).isEqualTo(1);
    assertThat(loadCount("id", clientId, "test_m_client_age")).isEqualTo(1);
    assertThat(loadCount("id", clientId, "test_m_client_amount")).isEqualTo(1);

    {
      Nf36Upserter upserter = upserterCreator.get().create();

      upserter.setTimeFieldName("ts");
      upserter.setNf3TableName("test_client");

      upserter.putId("id", clientId);
      upserter.putField("test_m_client_surname", "surname", "Петров");
      upserter.putField("test_m_client_name", "name", "Иван");
      upserter.putField("test_m_client_birth", "birth", ts("2000-01-17 17:23:34"));
      upserter.putField("test_m_client_age", "age", 6374562);
      upserter.putField("test_m_client_amount", "amount", 328478112862L);

      upserter.commit();
    }

    assertThat(loadStr("id", clientId, "test_client", "surname")).isEqualTo("Петров");
    assertThat(loadStr("id", clientId, "test_client", "name")).isEqualTo("Иван");
    assertThat(loadTS("id", clientId, "test_client", "birth")).isEqualTo("2000-01-17 17:23:34");
    assertThat(loadInt("id", clientId, "test_client", "age")).isEqualTo(6374562);
    assertThat(loadLong("id", clientId, "test_client", "amount")).isEqualTo(328478112862L);

    assertThat(loadLastStr("id", clientId, "test_m_client_surname", "surname")).isEqualTo("Петров");
    assertThat(loadLastStr("id", clientId, "test_m_client_name", "name")).isEqualTo("Иван");
    assertThat(loadLastTS("id", clientId, "test_m_client_birth", "birth")).isEqualTo("2000-01-17 17:23:34");
    assertThat(loadLastInt("id", clientId, "test_m_client_age", "age")).isEqualTo(6374562);
    assertThat(loadLastLong("id", clientId, "test_m_client_amount", "amount")).isEqualTo(328478112862L);

    assertThat(loadCount("id", clientId, "test_m_client_name"))
        .describedAs("It MUST be ONE (not two) because same value do not add")
        .isEqualTo(1);//MUST be ONE

    assertThat(loadCount("id", clientId, "test_m_client_surname")).isEqualTo(2);
    assertThat(loadCount("id", clientId, "test_m_client_birth")).isEqualTo(2);
    assertThat(loadCount("id", clientId, "test_m_client_age")).isEqualTo(2);
    assertThat(loadCount("id", clientId, "test_m_client_amount")).isEqualTo(2);
  }

  protected void createTablesForTestModificationInfo() {
    exec("create table test_home (       " +
        "  id int,                       " +
        "  name             varchar(300)," +
        "  address          varchar(500)," +
        "  created_at       timestamp default current_timestamp not null," +
        "  last_modified_at timestamp default current_timestamp not null," +
        "  author           varchar(100)," +
        "  last_modifier    varchar(100)," +
        "  primary key(id)" +
        ")");

    exec("create table test_m_home_name (" +
        "  id int," +
        "  ts timestamp default current_timestamp," +
        "  name         varchar(300)," +
        "  inserted_by  varchar(100)," +
        "  primary key(id, ts)" +
        ")");

    exec("create table test_m_home_address (" +
        "  id int," +
        "  ts timestamp default current_timestamp," +
        "  address      varchar(500)," +
        "  inserted_by  varchar(100)," +
        "  primary key(id, ts)" +
        ")");
  }

  @Test
  public void check_modification_info_updates() {

    exec("drop table test_m_home_name", DROP_TABLE);
    exec("drop table test_m_home_address", DROP_TABLE);
    exec("drop table test_home", DROP_TABLE);
    createTablesForTestModificationInfo();

    Date time1 = now();
    sleep(30);

    int clientId = RND.plusInt(1_000_000_000) + 1;

    {
      Nf36Upserter upserter = upserterCreator.get().create();

      upserter.setNf3TableName("test_home");
      upserter.setTimeFieldName("ts");
      upserter.setAuthorFieldNames("author", "last_modifier", "inserted_by");

      upserter.putId("id", clientId);
      upserter.putField("test_m_home_name", "name", "Мой дом");
      upserter.putField("test_m_home_address", "address", "Шаляпина Правды");

      upserter.putUpdateToNow("last_modified_at");

      upserter.setAuthor("Вселенная");

      upserter.commit();
    }

    assertThat(loadStr("id", clientId, "test_home", "name")).isEqualTo("Мой дом");
    assertThat(loadStr("id", clientId, "test_home", "address")).isEqualTo("Шаляпина Правды");
    assertThat(loadStr("id", clientId, "test_home", "author")).isEqualTo("Вселенная");
    assertThat(loadStr("id", clientId, "test_home", "last_modifier")).isEqualTo("Вселенная");

    assertThat(loadLastStr("id", clientId, "test_m_home_name", "name")).isEqualTo("Мой дом");
    assertThat(loadLastStr("id", clientId, "test_m_home_name", "inserted_by")).isEqualTo("Вселенная");

    assertThat(loadLastStr("id", clientId, "test_m_home_address", "address")).isEqualTo("Шаляпина Правды");
    assertThat(loadLastStr("id", clientId, "test_m_home_address", "inserted_by")).isEqualTo("Вселенная");

    Date createdAt1 = loadDate("id", clientId, "test_home", "created_at");
    Date lastModifiedAt1 = loadDate("id", clientId, "test_home", "last_modified_at");

    sleep(30);
    Date time2 = now();
    sleep(30);

    {
      Nf36Upserter upserter = upserterCreator.get().create();

      upserter.setNf3TableName("test_home");
      upserter.setTimeFieldName("ts");
      upserter.setAuthorFieldNames("author", "last_modifier", "inserted_by");

      upserter.putId("id", clientId);
      upserter.putField("test_m_home_name", "name", "Наш дом");
      upserter.putField("test_m_home_address", "address", "Шаляпина Правды");

      upserter.putUpdateToNow("last_modified_at");

      upserter.setAuthor("Строители");

      upserter.commit();
    }

    assertThat(loadStr("id", clientId, "test_home", "name")).isEqualTo("Наш дом");
    assertThat(loadStr("id", clientId, "test_home", "address")).isEqualTo("Шаляпина Правды");
    assertThat(loadStr("id", clientId, "test_home", "author")).isEqualTo("Вселенная");
    assertThat(loadStr("id", clientId, "test_home", "last_modifier")).isEqualTo("Строители");

    assertThat(loadLastStr("id", clientId, "test_m_home_name", "name")).isEqualTo("Наш дом");
    assertThat(loadLastStr("id", clientId, "test_m_home_name", "inserted_by")).isEqualTo("Строители");

    assertThat(loadLastStr("id", clientId, "test_m_home_address", "address")).isEqualTo("Шаляпина Правды");
    assertThat(loadLastStr("id", clientId, "test_m_home_address", "inserted_by")).isEqualTo("Вселенная");

    Date createdAt2 = loadDate("id", clientId, "test_home", "created_at");
    Date lastModifiedAt2 = loadDate("id", clientId, "test_home", "last_modified_at");

    sleep(30);
    Date time3 = now();

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

    System.out.println("time1           = " + sdf.format(time1));
    System.out.println("createdAt1      = " + sdf.format(createdAt1));
    System.out.println("createdAt2      = " + sdf.format(createdAt2));
    System.out.println("lastModifiedAt1 = " + sdf.format(lastModifiedAt1));
    System.out.println("time2           = " + sdf.format(time2));
    System.out.println("lastModifiedAt2 = " + sdf.format(lastModifiedAt2));
    System.out.println("time3           = " + sdf.format(time3));

    //time1 ... createdAt1, createdAt2 ... time2 ... time3

    assertThat(createdAt1).isBetween(time1, time2, false, false);
    assertThat(createdAt2).isBetween(time1, time2, false, false);

    //time1 ... lastModifiedAt1 ... time2 ... lastModifiedAt2 ... time3

    assertThat(lastModifiedAt1).isBetween(time1, time2, false, false);
    assertThat(lastModifiedAt2).isBetween(time2, time3, false, false);
  }
}
