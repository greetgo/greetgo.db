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
import java.util.Objects;

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
        "  ts          timestamp default current_timestamp," +
        "  surname     varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_name (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp default current_timestamp," +
        "  name        varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_patronymic (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp default current_timestamp," +
        "  patronymic  varchar(300),   " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_birth (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp default current_timestamp," +
        "  birth       timestamp,      " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_age (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp default current_timestamp," +
        "  age         int,            " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_amount (" +
        "  id1         int,            " +
        "  id2         varchar(32),    " +
        "  ts          timestamp default current_timestamp," +
        "  amount      bigint,         " +
        "  inserted_by varchar(100),   " +
        "  primary key(id1, id2, ts)   " +
        ")");

  }

  private Nf36Saver newSaver() {
    return saverCreator.get().create()

        .setNf3TableName("t014_client")
        .setTimeFieldName("ts")
        .setAuthorFieldNames("author", "last_modifier", "inserted_by")
        .putUpdateToNow("last_modified_at")

        .addIdName("id1")
        .addIdName("id2")

        .addFieldName("t014_client_surname", "surname")
        .addFieldName("t014_client_name", "name")
        .addFieldName("t014_client_patronymic", "patronymic")
        .addFieldName("t014_client_birth", "birth")
        .addFieldName("t014_client_age", "age")
        .addFieldName("t014_client_amount", "amount")

        ;
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

    //
    //
    newSaver()
        .setAuthor("Создатель")
        .save(c);
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

    //
    //
    newSaver()
        .setAuthor("Вселенная")
        .save(c);
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

  @Test
  public void skip_fields() {
    Client1 c = rndClient1();

    assertThat(c.name).isNotNull();
    assertThat(c.patronymic).isNotNull();

    String name[] = new String[]{null};

    //
    //
    newSaver()
        .addSkipIf("name", value -> {
          name[0] = (String) value;
          return true;
        })
        .save(c);
    //
    //

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "name")).isNull();
    assertThat(name[0]).isEqualTo(c.name);
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "patronymic")).isEqualTo(c.patronymic);
  }

  @Test
  public void skip_ifNull() {
    Client1 c = rndClient1();

    newSaver().save(c);

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "name")).isNotNull();
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "patronymic")).isNotNull();

    c.name = null;
    c.patronymic = null;

    //
    //
    newSaver()
        .addSkipIf("name", Objects::isNull)
        .save(c);
    //
    //

    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "name")).isNotNull();
    assertThat(loadStr("id1", c.id1, "id2", c.id2, "t014_client", "patronymic")).isNull();
  }

  public static class Client2 {
    public int idOne;
    public String id2;
    public String nameOfClient;
    public String patronymic;
  }

  @Test
  public void using_aliases() {

    Client2 c = new Client2();
    c.idOne = RND.plusInt(1_000_000_000);
    c.id2 = RND.str(10);
    c.nameOfClient = RND.str(10);
    c.patronymic = RND.str(10);

    //
    //
    newSaver()
        .addAlias("id1", "idOne")
        .addAlias("name", "nameOfClient")
        .save(c);
    //
    //

    assertThat(loadStr("id1", c.idOne, "id2", c.id2, "t014_client", "name")).isEqualTo(c.nameOfClient);

    c.nameOfClient = null;

    //
    //
    newSaver()
        .addAlias("id1", "idOne")
        .addAlias("name", "nameOfClient")
        .addSkipIf("name", s -> true)
        .save(c);
    //
    //

    assertThat(loadStr("id1", c.idOne, "id2", c.id2, "t014_client", "name")).isNotNull();
  }
}
