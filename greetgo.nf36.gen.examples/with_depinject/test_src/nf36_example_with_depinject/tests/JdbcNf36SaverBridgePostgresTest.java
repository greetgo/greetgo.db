package nf36_example_with_depinject.tests;

import kz.greetgo.db.nf36.core.Nf36Saver;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import kz.greetgo.util.RND;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.SaverCreator;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static nf36_example_with_depinject.errors.SqlError.Type.DROP_TABLE;
import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class JdbcNf36SaverBridgePostgresTest extends ParentDbTests {

  public BeanGetter<SaverCreator> saverCreator;

  @BeforeMethod
  public void recreateTables() {
    exec("drop table t014_client_surname    ", DROP_TABLE);
    exec("drop table t014_client_name       ", DROP_TABLE);
    exec("drop table t014_client_patronymic ", DROP_TABLE);
    exec("drop table t014_client_birth      ", DROP_TABLE);
    exec("drop table t014_client_age        ", DROP_TABLE);
    exec("drop table t014_client_amount     ", DROP_TABLE);
    exec("drop table t014_client              ", DROP_TABLE);

    createTables();
  }

  protected void createTables() {
    exec("create table t014_client (      " +
        //ids
        "  id1              int,          " +
        "  id2              varchar(32),  " +
        //data fields
        "  surname          varchar(300), " +
        "  name             varchar(300), " +
        "  patronymic       varchar(300), " +
        "  birth            timestamp,    " +
        "  age              int,          " +
        "  amount           bigint,       " +
        // status fields
        "  author           varchar(100), " +
        "  last_modifier    varchar(100), " +
        "  created_at       timestamp default current_timestamp not null, " +
        "  last_modified_at timestamp default current_timestamp not null, " +
        // etc
        "  primary key(id1, id2)" +
        ")");

    exec("create table t014_client_surname (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  surname     varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_name (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  name        varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_patronymic (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  patronymic  varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_birth (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  birth       timestamp,      " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_age (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  age         int,            " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_amount (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp,      " +
        "  amount      bigint,         " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

  }

  private Nf36Saver newSaver() {
    Nf36Saver saver = saverCreator.get().create();

    saver.addIdName("id1");
    saver.addIdName("id2");

    saver.addFieldName("t014_client_surname", "surname");
    saver.addFieldName("t014_client_name", "name");
    saver.addFieldName("t014_client_patronymic", "patronymic");
    saver.addFieldName("t014_client_birth", "birth");
    saver.addFieldName("t014_client_age", "age");
    saver.addFieldName("t014_client_amount", "amount");

    return saver;
  }

  public static class Client1 {
    public int id1;
    public String id2;
    public String surname;
    public String name;
    public String patronymic;
    public Date birth;
    public int age;
    public long amount;
  }

  private Client1 rndClient1() {
    Client1 ret = new Client1();
    ret.id1 = RND.plusInt(1_000_000_000);
    ret.id2 = RND.str(10);

    ret.surname = RND.str(10);
    ret.name = RND.str(10);
    ret.patronymic = RND.str(10);
    ret.birth = RND.dateYears(-100, -20);
    ret.age = RND.plusInt(1_000_000_000);
    ret.amount = RND.plusLong(1_000_000_000_000_000L);

    return ret;
  }

  @Test
  public void simple_fields_access() {

    Client1 c = rndClient1();

    Nf36Saver saver = newSaver();

    saver.putUpdateToNow("last_modified_at");
    saver.setAuthor("Создатель");

    //
    //
    saver.save(c);
    //
    //

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "surname")).isEqualTo(c.surname);
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "name")).isEqualTo(c.name);
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "patronymic")).isEqualTo(c.patronymic);
    assertThat(loadTS("id1", c.id1, "id2", c.id2, "t014_client", "birth")).isEqualTo(str(c.birth));
    assertThat(loadInt("id1", c.id1, "id2", c.id2, "t014_client", "age")).isEqualTo(c.age);
    assertThat(loadLong("id1", c.id1, "id2", c.id2, "t014_client", "amount")).isEqualTo(c.amount);

    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_surname", "surname")).isEqualTo(c.surname);
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_name", "name")).isEqualTo(c.name);
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_patronymic", "patronymic")).isEqualTo(c.patronymic);
    assertThat(loadLastTS("id1", c.id1, "id2", c.id2, "t014_client_birth", "birth")).isEqualTo(str(c.birth));
    assertThat(loadLastInt("id1", c.id1, "id2", c.id2, "t014_client_age", "age")).isEqualTo(c.age);
    assertThat(loadLastLong("id1", c.id1, "id2", c.id2, "t014_client_amount", "amount")).isEqualTo(c.amount);

    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_surname", "inserted_by")).isEqualTo("Создатель");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_name", "inserted_by")).isEqualTo("Создатель");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_patronymic", "inserted_by")).isEqualTo("Создатель");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_birth", "inserted_by")).isEqualTo("Создатель");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_age", "inserted_by")).isEqualTo("Создатель");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_amount", "inserted_by")).isEqualTo("Создатель");

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "author")).isEqualTo("Создатель");
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "last_modifier")).isEqualTo("Создатель");

    c.surname = RND.str(10);
    c.name = RND.str(10);
    c.patronymic = RND.str(10);
    c.birth = RND.dateYears(-100, -20);
    c.age = RND.plusInt(1_000_000_000);
    c.amount = RND.plusLong(1_000_000_000_000_000L);

    saver = newSaver();

    saver.putUpdateToNow("last_modified_at");
    saver.setAuthor("Вселенная");

    //
    //
    saver.save(c);
    //
    //

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "surname")).isEqualTo(c.surname);
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "name")).isEqualTo(c.name);
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "patronymic")).isEqualTo(c.patronymic);
    assertThat(loadTS("id1", c.id1, "id2", c.id2, "t014_client", "birth")).isEqualTo(str(c.birth));
    assertThat(loadInt("id1", c.id1, "id2", c.id2, "t014_client", "age")).isEqualTo(c.age);
    assertThat(loadLong("id1", c.id1, "id2", c.id2, "t014_client", "amount")).isEqualTo(c.amount);

    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_surname", "surname")).isEqualTo(c.surname);
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_name", "name")).isEqualTo(c.name);
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_patronymic", "patronymic")).isEqualTo(c.patronymic);
    assertThat(loadLastTS("id1", c.id1, "id2", c.id2, "t014_client_birth", "birth")).isEqualTo(str(c.birth));
    assertThat(loadLastInt("id1", c.id1, "id2", c.id2, "t014_client_age", "age")).isEqualTo(c.age);
    assertThat(loadLastLong("id1", c.id1, "id2", c.id2, "t014_client_amount", "amount")).isEqualTo(c.amount);

    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_surname", "inserted_by")).isEqualTo("Вселенная");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_name", "inserted_by")).isEqualTo("Вселенная");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_patronymic", "inserted_by")).isEqualTo("Вселенная");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_birth", "inserted_by")).isEqualTo("Вселенная");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_age", "inserted_by")).isEqualTo("Вселенная");
    assertThat(loadLastStr("id1", c.id1, "id2", c.id2, "t014_client_amount", "inserted_by")).isEqualTo("Вселенная");

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "author")).isEqualTo("Создатель");
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "last_modifier")).isEqualTo("Вселенная");
  }
}
