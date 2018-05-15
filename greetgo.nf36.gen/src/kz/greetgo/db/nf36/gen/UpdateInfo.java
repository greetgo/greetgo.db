package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.model.Nf3Field;

import java.io.File;
import java.util.List;

public interface UpdateInfo {
  String interfacePackageName();

  String interfaceClassName();

  File interfaceJavaFile();

  String implClassName();

  String implPackageName();

  File implJavaFile();

  List<Nf3Field> fields();
}
