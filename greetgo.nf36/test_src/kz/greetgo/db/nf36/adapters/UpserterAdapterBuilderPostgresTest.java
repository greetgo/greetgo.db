package kz.greetgo.db.nf36.adapters;

import kz.greetgo.db.DbType;
import kz.greetgo.db.nf36.util.ParentDbTest;
import kz.greetgo.db.nf36.util.Use;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@Use(DbType.Postgres)
public class UpserterAdapterBuilderPostgresTest extends ParentDbTest {

  @Test
  public void prepareDb() throws Exception {
    connector().prepareDatabase();

    System.out.println(connector());

    assertThat(1);
  }
}
