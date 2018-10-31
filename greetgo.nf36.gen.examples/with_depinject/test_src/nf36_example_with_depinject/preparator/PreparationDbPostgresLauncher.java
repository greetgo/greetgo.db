package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.bean_containers.BeanContainerPostgres;

public class PreparationDbPostgresLauncher {
  public static void main(String[] args) throws Exception {
    BeanContainerPostgres container = BeanContainerPostgres.create();

    container.dbWorker().recreateDb();

    container.generators()
        .generateSqlFiles()
        .forEach(container.dbWorker()::applySqlFile);
  }
}
