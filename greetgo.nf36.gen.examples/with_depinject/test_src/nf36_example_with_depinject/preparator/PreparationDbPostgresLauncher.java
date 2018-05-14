package nf36_example_with_depinject.preparator;

import kz.greetgo.db.nf36.gen.SqlDialectPostgres;
import kz.greetgo.db.worker.DbWorkerPostgres;
import nf36_example_with_depinject.generators.ExampleGenerators;
import nf36_example_with_depinject.util.DbConfigPostgres;

public class PreparationDbPostgresLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationDbPostgresLauncher().exec();
  }

  private void exec() throws Exception {
    DbWorkerPostgres dbWorker = new DbWorkerPostgres(DbConfigPostgres.instance());

    dbWorker.recreateDb();

    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators
      .generateSqlFiles(new SqlDialectPostgres())
      .forEach(dbWorker::applySqlFile);
  }
}
