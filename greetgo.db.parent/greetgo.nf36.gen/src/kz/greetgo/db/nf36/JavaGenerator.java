package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.util.List;

public class JavaGenerator {
  String interfaceOutDir;
  String interfaceBasePackage;

  String implOutDir;
  String implBasePackage;

  String sourceBasePackage;

  private JavaGenerator() {}

  public static JavaGenerator newGenerator() {
    return new JavaGenerator();
  }

  public JavaGenerator setOutDir(String outDir) {
    this.interfaceOutDir = outDir;
    this.implOutDir = outDir;
    return this;
  }

  public JavaGenerator setInterfaceOutDir(String interfaceOutDir) {
    this.interfaceOutDir = interfaceOutDir;
    return this;
  }

  public JavaGenerator setImplOutDir(String implOutDir) {
    this.implOutDir = implOutDir;
    return this;
  }

  public JavaGenerator setInterfaceBasePackage(String interfaceBasePackage) {
    this.interfaceBasePackage = interfaceBasePackage;
    return this;
  }

  public JavaGenerator setImplBasePackage(String implBasePackage) {
    this.implBasePackage = implBasePackage;
    return this;
  }

  public void generate(List<Nf3Table> nf3TableList) {

    cleanOutDirs();

    for (Nf3Table nf3Table : nf3TableList) {
      generateOne(nf3Table);
    }
  }

  private void cleanOutDirs() {
    UtilsNf36.cleanDir(implOutDir);
    UtilsNf36.cleanDir(interfaceOutDir);
  }

  public JavaGenerator setSourceBasePackage(String sourceBasePackage) {
    this.sourceBasePackage = sourceBasePackage;
    return this;
  }

  interface Nf3TableInfo {
    File interfaceJavaFile();

    String interfaceClassName();

    String interfacePackageName();

    File implJavaFile();

    String implPackageName();

    String implClassName();
  }


  Nf3TableInfo nf3TableInfo(Nf3Table nf3Table) {

    String subPackage = UtilsNf36.calcSubPackage(sourceBasePackage, nf3Table.source().getPackage().getName());

    String interfaceClassName = nf3Table.source().getSimpleName() + "Door";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = UtilsNf36.resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = UtilsNf36.resolveJavaFile(implOutDir, implPackageName, implClassName);

    return new Nf3TableInfo() {
      @Override
      public File interfaceJavaFile() {
        return interfaceJavaFile;
      }

      @Override
      public String interfaceClassName() {
        return interfaceClassName;
      }

      @Override
      public String interfacePackageName() {
        return interfacePackageName;
      }

      @Override
      public File implJavaFile() {
        return implJavaFile;
      }

      @Override
      public String implClassName() {
        return implClassName;
      }

      @Override
      public String implPackageName() {
        return implPackageName;
      }
    };
  }


  private void generateOne(Nf3Table nf3Table) {

    Nf3TableInfo info = nf3TableInfo(nf3Table);

    info.interfaceJavaFile().getParentFile().mkdirs();

    JavaFilePrinter interfaceFilePrinter = new JavaFilePrinter();
    interfaceFilePrinter.packageName = info.interfacePackageName();
    interfaceFilePrinter.classHeader = "public interface " + info.interfaceClassName();

    interfaceFilePrinter.printToFile(info.interfaceJavaFile());

    JavaFilePrinter implFilePrinter = new JavaFilePrinter();
    implFilePrinter.packageName = info.implPackageName();
    implFilePrinter.classHeader = "public class " + info.implClassName();

    implFilePrinter.printToFile(info.implJavaFile());
  }
}
