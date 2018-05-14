package kz.greetgo.db.nf36.gen.example.preparator;

import kz.greetgo.db.nf36.gen.example.generators.ExampleGenerators;

public class PreparationJavaLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationJavaLauncher().exec();
  }

  private void exec() throws Exception {
    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateJava();
  }
}
