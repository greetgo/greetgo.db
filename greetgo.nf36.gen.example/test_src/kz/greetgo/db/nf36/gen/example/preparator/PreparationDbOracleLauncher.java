package kz.greetgo.db.nf36.gen.example.preparator;

import kz.greetgo.db.nf36.gen.SqlDialectOracle;
import kz.greetgo.db.nf36.gen.example.generators.ExampleGenerators;
import kz.greetgo.db.nf36.gen.example.util.DbConfigOracle;
import kz.greetgo.db.worker.DbWorkerOracle;

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
