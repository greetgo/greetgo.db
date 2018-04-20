package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.errors.IllegalPackage;

import java.io.File;

public class UtilsNf36 {
  public static String calcSubPackage(String basePackageName, String packageName) {
    if (!packageName.startsWith(basePackageName)) throw new IllegalPackage(basePackageName, packageName);

    String sub = packageName.substring(basePackageName.length());

    if (sub.length() == 0) return null;

    if (sub.startsWith(".")) return sub.substring(1);

    throw new IllegalPackage(basePackageName, packageName);
  }

  public static String resolvePackage(String basePackage, String subPackage) {
    if (subPackage == null) return basePackage;
    return basePackage + "." + subPackage;
  }

  public static File resolveJavaFile(String srcDir, String packageName, String className) {
    return new File(srcDir + "/" + packageName.replace('.', '/') + "/" + className + ".java");
  }

  public static String extractSimpleName(String fullName) {
    int index = fullName.lastIndexOf('.');
    if (index < 0) return fullName;
    return fullName.substring(index + 1);
  }
}
