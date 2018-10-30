package kz.greetgo.db.nf36.gen;

import java.io.File;

public interface SaveInfo {
  File interfaceJavaFile();

  String interfaceClassName();

  String interfacePackageName();

  File implJavaFile();

  String implPackageName();

  String implClassName();

  String saveMethodName();
}
