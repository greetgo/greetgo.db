package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForOracleTests;

@ContainerConfig(BeanConfigForOracleTests.class)
public class JdbcNf36UpdaterAdapterOracleTests extends JdbcNf36UpdaterAdapterPostgresTests {

  @Override
  protected void createTableTmp1() {
    exec("create table tmp1 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  name1 varchar2(100)," +
        "  name2 varchar2(100)," +
        "  f1    varchar2(100) default 'def value' not null," +
        "  f2    varchar2(100) default 'def value' not null," +
        "  primary key(id1, id2)" +
        ")");
  }

  @Override
  protected void createTableTmp1f1() {
    exec("create table tmp1_f1 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  ts    timestamp default current_timestamp not null," +
        "  f1    varchar2(100) default 'def value' not null," +
        "  foreign key (id1, id2) references tmp1(id1, id2)," +
        "  primary key(id1, id2, ts)" +
        ")");
  }

  @Override
  protected void createTableTmp1f2() {
    exec("create table tmp1_f2 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  ts    timestamp default current_timestamp not null," +
        "  f2    varchar2(100)," +
        "  foreign key (id1, id2) references tmp1(id1, id2)," +
        "  primary key(id1, id2, ts)" +
        ")");
  }

  @Override
  protected void createTableAdam() {
    exec("create table adam (" +
        "  id         varchar2(32)," +
        "  surname    varchar2(300)," +
        "  name       varchar2(300)," +
        "  patronymic varchar2(300)," +
        "  primary key(id)" +
        ")");
  }

  @Override
  protected void createTableAdamSurname() {
    exec("create table adam_surname (" +
        "  id varchar2(32)," +
        "  ts timestamp default current_timestamp," +
        "  surname varchar2(300)," +
        "  foreign key (id) references adam(id)," +
        "  primary key(id, ts)" +
        ")");
  }

  @Override
  protected void createTableAdamName() {
    exec("create table adam_name (" +
        "  id varchar2(32)," +
        "  ts timestamp default current_timestamp," +
        "  name varchar2(300)," +
        "  foreign key (id) references adam(id)," +
        "  primary key(id, ts)" +
        ")");
  }

  @Override
  protected void createTableAdamPatronymic() {
    exec("create table adam_Patronymic (" +
        "  id varchar2(32)," +
        "  ts timestamp default current_timestamp," +
        "  Patronymic varchar2(300)," +
        "  foreign key (id) references adam(id)," +
        "  primary key(id, ts)" +
        ")");
  }

  protected void createTableTmp2() {
    exec("create table tmp2 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  name1 varchar2(100)," +
        "  name2 varchar2(100)," +
        "  f1    varchar2(100)," +
        "  f2    varchar2(100)," +
        "  last_modified_by varchar2(100) default 'no soul'," +
        "  last_modified_at timestamp default current_timestamp not null," +
        "  mod_at timestamp," +
        "  primary key(id1, id2)" +
        ")");
  }

  protected void createTableTmp2_f1() {
    exec("create table tmp2_f1 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  ts    timestamp default current_timestamp not null," +
        "  f1    varchar2(100)," +
        "  inserted_by varchar2(100) default 'no soul'," +
        "  primary key(id1, id2, ts)" +
        ")");
  }

  protected void createTableTmp2_f2() {
    exec("create table tmp2_f2 (" +
        "  id1   varchar2(32)," +
        "  id2   varchar2(32)," +
        "  ts    timestamp default current_timestamp not null," +
        "  f2    varchar2(100)," +
        "  inserted_by varchar2(100)," +
        "  primary key(id1, id2, ts)" +
        ")");
  }


}
