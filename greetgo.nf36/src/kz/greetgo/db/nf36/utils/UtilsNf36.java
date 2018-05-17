package kz.greetgo.db.nf36.utils;

import kz.greetgo.db.nf36.errors.IllegalPackage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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

  public static <E> List<E> mutableList(E element) {
    List<E> ret = new ArrayList<>();
    ret.add(element);
    return ret;
  }

  public static String firstToLow(String str) {
    if (str == null) return null;
    if (str.length() < 2) return str.toLowerCase();
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  public static String firstToUp(String str) {
    if (str == null) return null;
    if (str.length() < 2) return str.toUpperCase();
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  public static String quoteForSql(String comment) {
    return comment.replaceAll("'", "''");
  }

  public static String selectName(String wantName, Set<String> anotherNames) {
    if (wantName == null) wantName = "a";
    if (!anotherNames.contains(wantName)) return wantName;
    int i = 1;
    while (true) {
      String ret = wantName + i++;
      if (!anotherNames.contains(ret)) return ret;
    }
  }

  public static String readAll(Reader characterStream) {

    try {

      while (true) {
        StringBuilder sb = new StringBuilder();
        char buffer[] = new char[1024 * 4];
        int count = characterStream.read(buffer);
        if (count < 0) return sb.toString();
        sb.append(buffer, 0, count);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SafeVarargs
  public static <T> Stream<T> cat(Collection<T>... collections) {

    Stream<T> s = null;

    for (Collection<T> col : collections) {
      if (s == null) s = col.stream();
      else s = Stream.concat(s, col.stream());
    }

    return s == null ? Stream.empty() : s;

  }
}
