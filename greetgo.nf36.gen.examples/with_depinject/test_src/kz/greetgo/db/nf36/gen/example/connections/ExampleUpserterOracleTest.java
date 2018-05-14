package kz.greetgo.db.nf36.gen.example.connections;

import kz.greetgo.db.nf36.gen.example.beans.oracle.BeanConfigOracle;
import kz.greetgo.depinject.testng.ContainerConfig;

@ContainerConfig(BeanConfigOracle.class)
public class ExampleUpserterOracleTest extends ExampleUpserterPostgresTest {}
