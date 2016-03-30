package kz.greetgo.gbatis2.gen_sql.postgres;

import kz.greetgo.gbatis2.gen_sql.DdlVisitorToSql;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTable;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTableField;
import kz.greetgo.gbatis2.struct.model.std.StdLong;
import kz.greetgo.gbatis2.struct.model.std.StdStr;
import org.testng.annotations.Test;

public class DdlVisitorToSql_postgresTest {
  @Test
  public void createTable() throws Exception {

    CreateTable createTable = new CreateTable("asd");

    createTable.fields.add(new CreateTableField("id1", StdLong.INSTANCE, true, null));
    createTable.fields.add(new CreateTableField("id2", StdLong.INSTANCE, true, null));

    createTable.fields.add(new CreateTableField("surname", new StdStr(100), false, null));

    createTable.primaryKey.add("id1");
    createTable.primaryKey.add("id2");

    DdlVisitorToSql visitor = new DdlVisitorToSql_postgres();

    String sql = createTable.visit(visitor);

    System.out.println(sql);

  }
}