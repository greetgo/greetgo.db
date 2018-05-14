package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.generators.ExampleGenerators;

public class PreparationJavaLauncher {
  public static void main(String[] args) throws Exception {
    new PreparationJavaLauncher().exec();
  }

  private void exec() throws Exception {
    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateJava();
  }
}
