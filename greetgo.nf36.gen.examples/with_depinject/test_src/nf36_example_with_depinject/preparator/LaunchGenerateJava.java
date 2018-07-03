package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.bean_containers.BeanContainerOracle;
import nf36_example_with_depinject.bean_containers.BeanContainerPostgres;

public class LaunchGenerateJava {
  public static void main(String[] args) throws Exception {
    BeanContainerPostgres.create().generators().generateJava();
    BeanContainerOracle.create().generators().generateJava();
  }
}
