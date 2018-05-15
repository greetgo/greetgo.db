package kz.greetgo.db.nf36.gen;

import java.io.File;

public interface UpdateInfo {
  String interfacePackageName();

  String interfaceClassName();

  File interfaceJavaFile();

  String implClassName();

  String implPackageName();

  File implJavaFile();
}
