package nf36_example_with_depinject.preparator;

import nf36_example_with_depinject.bean_containers.BeanContainerPostgres;

public class PreparationPostgresJavaLauncher {
  public static void main(String[] args) throws Exception {
    BeanContainerPostgres.create().generators().generateJava();
  }
}
