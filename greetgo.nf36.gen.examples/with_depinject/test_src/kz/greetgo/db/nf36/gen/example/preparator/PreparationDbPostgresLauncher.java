package kz.greetgo.db.nf36.gen.example.preparator;

import kz.greetgo.db.nf36.gen.SqlDialectPostgres;
import kz.greetgo.db.nf36.gen.example.generators.ExampleGenerators;
import kz.greetgo.db.nf36.gen.example.util.DbConfigPostgres;
import kz.greetgo.db.worker.DbWorkerPostgres;

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
