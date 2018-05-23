package nf36_example_with_depinject.bean_containers.for_tests;

import kz.greetgo.db.worker.oracle.BeanConfigDbWorkerOracle;
import kz.greetgo.depinject.core.BeanConfig;
import kz.greetgo.depinject.core.Include;
import nf36_example_with_depinject.beans.oracle.BeanConfigOracle;

@BeanConfig
@Include({
  BeanConfigOracle.class,
  BeanConfigDbWorkerOracle.class,
})
public class BeanConfigForOracleTests {}
