package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.bean_containers.BeanContainerOracle;

public class PreparationOracleJavaLauncher {
  public static void main(String[] args) throws Exception {
    BeanContainerOracle.create().generators().generateJava();
  }
}
