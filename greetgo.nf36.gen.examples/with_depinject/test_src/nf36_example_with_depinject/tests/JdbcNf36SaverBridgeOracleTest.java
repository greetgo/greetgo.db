package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForOracleTests;
import org.testng.annotations.BeforeMethod;

@ContainerConfig(BeanConfigForOracleTests.class)
public class JdbcNf36SaverBridgeOracleTest extends JdbcNf36SaverBridgePostgresTest {

  @Override
  protected void createTables() {
    exec("create table t014_client (       " +
        //ids
        "  id1              int,           " +
        "  id2              varchar2(32),  " +
        //data fields
        "  surname          varchar2(300), " +
        "  name             varchar2(300), " +
        "  super_top_name   varchar2(300), " +
        "  patronymic       varchar2(300), " +
        "  birth            timestamp,     " +
        "  age              int,           " +
        "  amount           number(19),    " +
        // status fields
        "  author           varchar2(100), " +
        "  last_modifier    varchar2(100), " +
        "  created_at       timestamp default current_timestamp not null, " +
        "  last_modified_at timestamp default current_timestamp not null, " +
        // etc
        "  primary key(id1, id2)" +
        ")");

    exec("create table t014_client_surname (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  surname     varchar2(300),  " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_name (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  name        varchar2(300),  " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_super_top_name (" +
        "  id1            int,            " +
        "  id2            varchar2(32),   " +
        "  ts             timestamp default current_timestamp," +
        "  super_top_name varchar2(300),  " +
        "  inserted_by    varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_patronymic (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  patronymic  varchar2(300),  " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_birth (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  birth       timestamp,      " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_age (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  age         int,            " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

    exec("create table t014_client_amount (" +
        "  id1         int,            " +
        "  id2         varchar2(32),   " +
        "  ts          timestamp default current_timestamp," +
        "  amount      number(19),     " +
        "  inserted_by varchar2(100),  " +
        "  primary key(id1, id2, ts)   " +
        ")");

  }
}
