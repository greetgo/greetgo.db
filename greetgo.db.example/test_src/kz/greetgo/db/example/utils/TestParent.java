package kz.greetgo.db.example.utils;

import kz.greetgo.db.example.infrastructure.BeanConfigTests;
import kz.greetgo.depinject.testng.AbstractDepinjectTestNg;
import kz.greetgo.depinject.testng.ContainerConfig;

@ContainerConfig(BeanConfigTests.class)
public class TestParent extends AbstractDepinjectTestNg {}
