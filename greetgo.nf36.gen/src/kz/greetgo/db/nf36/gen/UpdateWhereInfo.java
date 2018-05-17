package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;

import java.io.File;
import java.util.List;

public interface UpdateWhereInfo {
  String interfacePackageName();

  String interfaceClassName();

  File interfaceJavaFile();

  String implClassName();

  String implPackageName();

  File implJavaFile();

  List<Nf3Field> fields();

  String interfaceFullName();

  String updateMethodName();

  String whereMethodName(Nf3Field f);

  String setMethodName(Nf3Field f);

  String implFullName();

  String nf3TableName();

  Class<?> source();

  String nf6TableName(Nf3Field f);

  String commitMethodName();
}
