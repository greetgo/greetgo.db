package kz.greetgo.gbatis2.gen_sql;

import kz.greetgo.gbatis2.gen_sql.ddl.Ddl;
import kz.greetgo.gbatis2.gen_sql.struct.StructAnchor;
import kz.greetgo.gbatis2.struct.DbStruct;
import kz.greetgo.gbatis2.struct.DbStructReader;
import kz.greetgo.gbatis2.struct.resource.ClassResourceRef;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;
import org.testng.annotations.Test;

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
}
