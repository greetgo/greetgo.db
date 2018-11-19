package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;

import java.io.File;
import java.util.List;

import static kz.greetgo.db.nf36.utils.UtilsNf36.resolveFullName;

public interface HistorySelectorInfo {
  String interfacePackageName();

  String interfaceClassName();

  File interfaceJavaFile();

  default String interfaceFullName() {
    return resolveFullName(interfacePackageName(), interfaceClassName());
  }

  String implPackageName();

  String implClassName();

  File implJavaFile();

  List<Nf3Field> fields();

  String atMethodName();

  Class<?> source();

  String toSuffix();

  String nf6TableName(Nf3Field f);

  String nf3TableName();
}
