package nf36_example_with_depinject.tests;

import kz.greetgo.depinject.testng.ContainerConfig;
import nf36_example_with_depinject.bean_containers.for_tests.BeanConfigForOracleTests;

@ContainerConfig(BeanConfigForOracleTests.class)
public class ExampleUpserterOracleTest extends ExampleUpserterPostgresTest {}
