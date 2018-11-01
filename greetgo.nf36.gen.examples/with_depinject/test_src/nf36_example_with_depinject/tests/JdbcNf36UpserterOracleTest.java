package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForOracleTests;

@ContainerConfig(BeanConfigForOracleTests.class)
public class JdbcNf36UpserterOracleTest extends JdbcNf36UpserterPostgresTest {
  protected void createTablesForTestFields() {
    exec("create table t13_client (" +
        "  id varchar2(32) not null," +
        "  surname varchar2(300)," +
        "  name varchar2(300)," +
        "  birth timestamp," +
        "  age int," +
        "  amount number(19)," +
        "" +
        "  primary key(id)" +
        ")");

    exec("create table t13_m_client_surname (" +
        "  id varchar2(32) not null references t13_client," +
        "  ts timestamp default current_timestamp not null," +
        "  surname varchar2(300)," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table t13_m_client_name (" +
        "  id varchar2(32) not null references t13_client," +
        "  ts timestamp default current_timestamp not null," +
        "  name varchar2(300)," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table t13_m_client_birth (" +
        "  id varchar2(32) not null references t13_client," +
        "  ts timestamp default current_timestamp not null," +
        "  birth timestamp," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table t13_m_client_age (" +
        "  id varchar2(32) not null references t13_client," +
        "  ts timestamp default current_timestamp not null," +
        "  age int," +
        "" +
        "  primary key(id, ts)" +
        ")");

    exec("create table t13_m_client_amount (" +
        "  id varchar2(32) not null references t13_client," +
        "  ts timestamp default current_timestamp not null," +
        "  amount number(19)," +
        "" +
        "  primary key(id, ts)" +
        ")");
  }

  @Override
  protected void createTablesForTestModificationInfo() {
    exec("create table t13_home (" +
        "  id int,                        " +
        "  name             varchar2(300)," +
        "  address          varchar2(500)," +
        "  created_at       timestamp default current_timestamp not null," +
        "  last_modified_at timestamp default current_timestamp not null," +
        "  author           varchar2(100)," +
        "  last_modifier    varchar2(100)," +
        "  primary key(id)" +
        ")");

    exec("create table t13_m_home_name (" +
        "  id int," +
        "  ts timestamp default current_timestamp," +
        "  name         varchar2(300)," +
        "  inserted_by  varchar2(100)," +
        "  primary key(id, ts)" +
        ")");

    exec("create table t13_m_home_address (" +
        "  id int," +
        "  ts timestamp default current_timestamp," +
        "  address      varchar2(500)," +
        "  inserted_by  varchar2(100)," +
        "  primary key(id, ts)" +
        ")");
  }
}
