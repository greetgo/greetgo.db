package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.beans.oracle.BeanConfigOracle;

@ContainerConfig(BeanConfigOracle.class)
public class ExampleWhereUpdaterOracleTest extends ExampleWhereUpdaterPostgresTest {}
