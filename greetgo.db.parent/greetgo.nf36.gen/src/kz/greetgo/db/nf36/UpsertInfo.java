package kz.greetgo.db.nf36;

import java.io.File;

interface UpsertInfo {
  File interfaceJavaFile();

  String interfaceClassName();

  String interfacePackageName();

  File implJavaFile();

  String implPackageName();

  String implClassName();

  String upsertMethodName();

  String interfaceFullName();

  String implFullName();
}
