package kz.greetgo.gbatis2.gen_sql;

import kz.greetgo.gbatis2.gen_sql.ddl.Ddl;
import kz.greetgo.gbatis2.gen_sql.struct.StructAnchor;
import kz.greetgo.gbatis2.struct.DbStruct;
import kz.greetgo.gbatis2.struct.DbStructReader;
import kz.greetgo.gbatis2.struct.resource.ClassResourceRef;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;
import org.testng.annotations.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbStructToDdlTest {
  @Test
  public void test() throws Exception {
    ResourceRef testDbStruct = ClassResourceRef.create(StructAnchor.class, "test.dbStruct");

    DbStruct dbStruct = DbStructReader.load(testDbStruct);

    DbStructToDdl g = new DbStructToDdl(dbStruct);

    g.generateDDL();

    for (Ddl ddl : g.ddlList) {
      System.out.println(ddl);
    }
  }

  @Test
  public void ins() throws Exception {

    Class.forName("org.postgresql.Driver");
    try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/gcorebs", "gcorebs", "gcorebs")) {

      try (CallableStatement cs = con.prepareCall("{call aaa_set_row(?)}")) {
        cs.setString(1, "ПРИВЕТ Мир, Hi World");

        cs.execute();
      }
    }

  }
}
