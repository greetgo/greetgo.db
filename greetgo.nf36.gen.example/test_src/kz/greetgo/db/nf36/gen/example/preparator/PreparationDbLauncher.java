package kz.greetgo.db.nf36.gen.example.preparator;

import kz.greetgo.db.nf36.gen.example.generators.ExampleGenerators;
import kz.greetgo.db.nf36.gen.example.util.PostgresDbConfig;
import kz.greetgo.db.worker.DbWorker;

public class PreparationDbLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationDbLauncher().exec();
  }

  private void exec() throws Exception {
    DbWorker dbWorker = new DbWorker(PostgresDbConfig.instance());

    dbWorker.recreateDb();

    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateSqlFiles().forEach(dbWorker::applySqlFile);
  }
}
