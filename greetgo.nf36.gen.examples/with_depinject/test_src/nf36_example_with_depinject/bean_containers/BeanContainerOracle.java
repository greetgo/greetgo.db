package nf36_example_with_depinject.bean_containers;

import kz.greetgo.db.worker.util.Places;
import kz.greetgo.db.worker.oracle.BeanConfigDbWorkerOracle;
import kz.greetgo.db.worker.oracle.DbWorkerOracle;
import kz.greetgo.depinject.Depinject;
import kz.greetgo.depinject.NoImplementor;
import kz.greetgo.depinject.core.BeanContainer;
import kz.greetgo.depinject.core.Include;
import kz.greetgo.depinject.gen.DepinjectUtil;
import nf36_example_with_depinject.conf.oracle.BeanConfigOracleConf;
import nf36_example_with_depinject.generators.BeanConfigGenerators;
import nf36_example_with_depinject.generators.ExampleGenerators;

@Include({
  BeanConfigGenerators.class,
  BeanConfigOracleConf.class,
  BeanConfigDbWorkerOracle.class,
})
public interface BeanContainerOracle extends BeanContainer {

  static BeanContainerOracle create() throws Exception {
    try {
      return Depinject.newInstance(BeanContainerOracle.class);
    } catch (NoImplementor ignore) {
      DepinjectUtil.implementAndUseBeanContainers(
        "nf36_example_with_depinject.bean_containers",
        Places.withDepinjectDir() + "/build/bean_containers_impl");
      return Depinject.newInstance(BeanContainerOracle.class);
    }
  }

  DbWorkerOracle dbWorker();

  ExampleGenerators generators();
}
