package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.bean_containers.BeanContainerOracle;

public class PreparationDbOracleLauncher {
  public static void main(String[] args) throws Exception {

    BeanContainerOracle container = BeanContainerOracle.create();

    container.dbWorker().recreateDb();

    container.generators()
        .generateSqlFiles()
        .forEach(container.dbWorker()::applySqlFile);

  }
}
