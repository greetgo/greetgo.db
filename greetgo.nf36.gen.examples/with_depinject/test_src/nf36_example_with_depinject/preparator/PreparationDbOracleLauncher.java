package nf36_example_with_depinject.preparator;

import kz.greetgo.db.nf36.gen.SqlDialectOracle;
import kz.greetgo.db.worker.DbWorkerOracle;
import nf36_example_with_depinject.generators.ExampleGenerators;
import nf36_example_with_depinject.util.DbConfigOracle;

public class PreparationDbOracleLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationDbOracleLauncher().exec();
  }

  private void exec() throws Exception {
    DbWorkerOracle dbWorker = new DbWorkerOracle(DbConfigOracle.instance());

    dbWorker.recreateDb();

    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators
      .generateSqlFiles(new SqlDialectOracle())
      .forEach(dbWorker::applySqlFile);
  }
}
