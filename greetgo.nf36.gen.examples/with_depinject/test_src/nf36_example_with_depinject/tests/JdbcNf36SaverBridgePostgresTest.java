package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForPostgresTests;
import nf36_example_with_depinject.beans.all.UpserterCreator;
import nf36_example_with_depinject.util.ParentDbTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static nf36_example_with_depinject.errors.SqlError.Type.DROP_TABLE;
import static org.fest.assertions.api.Assertions.assertThat;

@ContainerConfig(BeanConfigForPostgresTests.class)
public class JdbcNf36SaverBridgePostgresTest extends ParentDbTests {

  public BeanGetter<UpserterCreator> upserterCreator;

  @BeforeMethod
  public void recreateTables() {
    exec("drop table t014_client", DROP_TABLE);

    createTables();
  }

  protected void createTables() {
    exec("create table t014_client (" +
        "  id1        int,          " +
        "  id2        varchar(32),  " +
        "  surname    varchar(300), " +
        "  name       varchar(300), " +
        "  patronymic varchar(300), " +
        "  birth      timestamp,    " +
        "  age        int,          " +
        "  amount     bigint,       " +
        "  primary key(id1, id2)    " +
        ")");

    exec("create table t014_m_client_surname (" +
        "  id1        int,            " +
        "  id2        varchar(32),    " +
        "  ts         timestamp,      " +
        "  surname    varchar(300),   " +
        "  primary key(id1, id2, ts)  " +
        ")");
  }

  @Test
  public void name() {

    System.out.println(upserterCreator.get());

    assertThat(1);
  }
}
