package kz.greetgo.gbatis2.gen_sql.postgres;

import kz.greetgo.gbatis2.gen_sql.DdlVisitorToSql;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateSequence;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTable;
import kz.greetgo.gbatis2.gen_sql.ddl.CreateTableField;
import kz.greetgo.gbatis2.gen_sql.ddl.DefaultCurrentTimestamp;
import kz.greetgo.gbatis2.struct.model.std.StdLong;
import kz.greetgo.gbatis2.struct.model.std.StdStr;
import kz.greetgo.gbatis2.struct.model.std.StdTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DdlVisitorToSql_postgresTest {

  private DdlVisitorToSql visitor;

  @BeforeMethod
  public void setUp() throws Exception {
    visitor = new DdlVisitorToSql_postgres();
  }

  @Test
  public void createTable() throws Exception {
    CreateTable createTable = new CreateTable("asd");

    createTable.fields.add(new CreateTableField("id1", StdLong.INSTANCE, true, null));
    createTable.primaryKey.add("id1");
    createTable.fields.add(new CreateTableField("id2", StdLong.INSTANCE, true, null));
    createTable.primaryKey.add("id2");
    createTable.fields.add(new CreateTableField("createdAt", StdTime.INSTANCE, true, DefaultCurrentTimestamp.INSTANCE));
    createTable.fields.add(new CreateTableField("surname", new StdStr(100), false, null));
    
    String sql = createTable.visit(visitor);
    System.out.println(sql);
  }

  @Test
  public void createSequence() throws Exception {
    CreateSequence createSequence = new CreateSequence("asd");
    String sql = createSequence.visit(visitor);
    System.out.println(sql);
  }
}
