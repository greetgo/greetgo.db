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

  public static void cleanDir(String dir) {
    removeFile(new File(dir), false);
  }

  private static void removeFile(File file, boolean killMe) {

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null) for (File subFile : files) {
        removeFile(subFile, true);
      }
    }

    if (killMe) file.delete();
  }

  public static String resolveFullName(String packageName, String className) {
    if (packageName == null) return className;
    return packageName + "." + className;
  }

  public static String javaNameToDbName(String javaName) {
    int sourceLen = javaName.length();
    char source[] = new char[sourceLen];
    javaName.getChars(0, sourceLen, source, 0);
    char dest[] = new char[source.length * 2];
    int j = 0;
    for (int i = 0; i < sourceLen; i++) {
      char c = source[i];
      char lowC = Character.toLowerCase(c);
      if (lowC == c) {
        dest[j++] = lowC;
      } else {
        dest[j++] = '_';
        dest[j++] = lowC;
      }
    }
    return new String(dest, 0, j);
  }

  public static String packageDir(String srcDir, String packageName) {
    if (packageName == null) return srcDir;
    return srcDir + "/" + packageName.replace('.', '/');
  }
}
