package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.nf36.util.Use;

import static kz.greetgo.db.nf36.util.SqlErrorType.DROP_TABLE;

@SuppressWarnings("SqlResolve")
@Use(DbType.Oracle)
public class UpserterAdapterBuilderOracleTest extends UpserterAdapterBuilderPostgresTest {

  @Override
  protected void createClientTables() {
    exec("drop table m_client_surname", DROP_TABLE);
    exec("drop table m_client_name", DROP_TABLE);
    exec("drop table m_client_father_name", DROP_TABLE);
    exec("drop table client", DROP_TABLE);

    exec("create table client (\n" +
        "  id          varchar2(32),\n" +
        "  surname     varchar2(300),\n" +
        "  name        varchar2(300),\n" +
        "  father_name varchar2(300),\n" +
        "  primary key (id)\n" +
        ")");
    exec("create table m_client_surname (\n" +
        "  id      varchar2(32) references client,\n" +
        "  ts      timestamp default current_timestamp,\n" +
        "  author  varchar2(100),\n" +
        "  surname varchar2(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
    exec("create table m_client_name (\n" +
        "  id     varchar2(32) references client,\n" +
        "  ts     timestamp default current_timestamp,\n" +
        "  author varchar2(100),\n" +
        "  name   varchar2(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
    exec("create table m_client_father_name (\n" +
        "  id          varchar2(32) references client,\n" +
        "  ts          timestamp default current_timestamp,\n" +
        "  author      varchar2(100),\n" +
        "  father_name varchar2(300),\n" +
        "  primary key (id, ts)\n" +
        ")");
  }
}
