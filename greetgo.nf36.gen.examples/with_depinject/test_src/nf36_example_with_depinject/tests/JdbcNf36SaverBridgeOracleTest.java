package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForOracleTests;
import org.testng.annotations.BeforeMethod;

@ContainerConfig(BeanConfigForOracleTests.class)
public class JdbcNf36SaverBridgeOracleTest extends JdbcNf36SaverBridgePostgresTest {

  @Override
  protected void createTables() {

  }
}
