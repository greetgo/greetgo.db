package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.generators.ExampleGenerators;

public class PreparationJavaLauncher {
  public static void main(String[] args) {
    new PreparationJavaLauncher().exec();
  }

  private void exec() {
    ExampleGenerators exampleGenerators = new ExampleGenerators();

    exampleGenerators.generateJava();

    System.out.println("Java generated OK");
  }
}
