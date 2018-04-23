package kz.greetgo.db.nf36.gen.example.preparator;

import kz.greetgo.db.nf36.gen.example.generators.ExampleGenerators;

public class PreparationDbLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationDbLauncher().exec();
  }

  private void exec() throws Exception {
    DbWorker dbWorker = new DbWorker();

    dbWorker.recreateDb();

    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateSqlFiles().forEach(dbWorker::applySqlFile);
  }
}
