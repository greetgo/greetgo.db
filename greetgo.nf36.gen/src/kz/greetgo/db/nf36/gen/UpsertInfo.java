package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;

import java.io.File;
import java.util.List;

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

  String moreMethodName();

  String commitMethodName();

  List<Nf3Field> fields();

  String nf3TableName();

  Class<?> source();

  String nf6TableName(Nf3Field f);
}
