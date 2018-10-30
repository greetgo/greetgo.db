package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;

import java.io.File;
import java.util.List;

public interface SaveInfo {
  File interfaceJavaFile();

  String interfaceClassName();

  String interfacePackageName();

  String interfaceFullName();

  File implJavaFile();

  String implPackageName();

  String implClassName();

  String saveMethodName();

  List<Nf3Field> fields();

  String nf3TableName();

  String nf6TableName(Nf3Field field);

  String accessToEntityMethodName();
}
