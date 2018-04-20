package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.utils.SqlUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaGenerator {
  String interfaceOutDir;
  String interfaceBasePackage;

  String implOutDir;
  String implBasePackage;

  String sourceBasePackage;

  String mainNf36ClassName;

  String mainNf36ImplClassName;

  JdbcDefinition jdbcDefinition;

  private JavaGenerator() {}

  public static JavaGenerator newGenerator() {
    return new JavaGenerator();
  }

  public JavaGenerator setOutDir(String outDir) {
    this.interfaceOutDir = outDir;
    this.implOutDir = outDir;
    return this;
  }

  public JavaGenerator setMainNf36ClassName(String mainNf36ClassName) {
    this.mainNf36ClassName = mainNf36ClassName;
    this.mainNf36ImplClassName = mainNf36ClassName + "Impl";
    return this;
  }

  @SuppressWarnings("unused")
  public JavaGenerator setMainNf36ImplClassName(String mainNf36ImplClassName) {
    this.mainNf36ImplClassName = mainNf36ImplClassName;
    return this;
  }

  public JavaGenerator setJdbcDefinition(JdbcDefinition jdbcDefinition) {
    this.jdbcDefinition = jdbcDefinition;
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
    check();

    if (cleanOutDirsBeforeGeneration) cleanOutDirs();

    String mainInterfaceClassName = generateMainInterface(nf3TableList);
    generateMainImpl(nf3TableList, mainInterfaceClassName);

    for (Nf3Table nf3Table : nf3TableList) {
      UpsertInfo info = getUpsertInfo(nf3Table);

      generateUpsertInterface(info, nf3Table);
      generateUpsertImpl(info, nf3Table);
    }
  }

  private void check() {
    if (jdbcDefinition == null) {
      throw new RuntimeException("Please call method JavaGenerator.setJdbcDefinition(JdbcDefinition)");
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

  boolean cleanOutDirsBeforeGeneration = true;

  public JavaGenerator setCleanOutDirsBeforeGeneration(boolean cleanOutDirsBeforeGeneration) {
    this.cleanOutDirsBeforeGeneration = cleanOutDirsBeforeGeneration;
    return this;
  }

  UpsertInfo getUpsertInfo(Nf3Table nf3Table) {

    String subPackage = UtilsNf36.calcSubPackage(sourceBasePackage, nf3Table.source().getPackage().getName());

    subPackage = UtilsNf36.resolvePackage("upsert", subPackage);

    String interfaceClassName = nf3Table.source().getSimpleName() + "Upsert";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = UtilsNf36.resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = UtilsNf36.resolveJavaFile(implOutDir, implPackageName, implClassName);

    String upsertMethodName = "upsert" + nf3Table.source().getSimpleName();

    String interfaceName = UtilsNf36.resolveFullName(interfacePackageName, interfaceClassName);

    String implName = UtilsNf36.resolveFullName(implPackageName, implClassName);

    return new UpsertInfo() {
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

      @Override
      public String upsertMethodName() {
        return upsertMethodName;
      }

      @Override
      public String interfaceFullName() {
        return interfaceName;
      }

      @Override
      public String implFullName() {
        return implName;
      }
    };
  }

  private void generateUpsertInterface(UpsertInfo info, Nf3Table nf3Table) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.interfacePackageName();
    p.classHeader = "public interface " + info.interfaceClassName();

    List<Nf3Field> fields = nf3Table.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaTypeName());
      String fieldName = f.javaName();
      p.ofs(1).prn(info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ");");
    }

    p.printToFile(info.interfaceJavaFile());
  }

  String fIdMap = "idMap";
  String fFieldMap = "fieldMap";

  private void generateUpsertImpl(UpsertInfo info, Nf3Table nf3Table) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.implPackageName();
    p.classHeader = "public class " + info.implClassName() + " implements "
      + p.i(UtilsNf36.resolveFullName(info.interfacePackageName(), info.interfaceClassName()));

    String mapClass = p.i(Map.class.getName());
    String hashMapClass = p.i(HashMap.class.getName());
    String sqlUtilClass = p.i(SqlUtil.class.getName());

    p.ofs(1).prn("private final " + mapClass + "<String, Object> " + fIdMap + " = new " + hashMapClass + "<>();");

    printUpsertImplConstructor(p, info, nf3Table);

    p.ofs(1).prn("private final " + mapClass + "<String, Object> " + fFieldMap + " = new " + hashMapClass + "<>();");
    p.prn();

    List<Nf3Field> fields = nf3Table.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaTypeName());
      String fieldName = f.javaName();
      p.ofs(1).prn("@Override");
      p.ofs(1).prn("public " + info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ") {");
      p.ofs(2).prn(fFieldMap + ".put(\"" + f.dbName() + "\", " + sqlUtilClass + ".forSql(" + fieldName + "));");
      p.ofs(2).prn("return this;");
      p.ofs(1).prn("}").prn();
    }

    p.printToFile(info.implJavaFile());
  }

  private void printUpsertImplConstructor(JavaFilePrinter p, UpsertInfo info, Nf3Table nf3Table) {
    String jdbcVar = jdbcDefinition.jdbcVar();
    String jdbcClass = p.i(jdbcDefinition.jdbcClassName());
    String sqlUtilClass = p.i(SqlUtil.class.getName());

    p.ofs(1).prn("private final " + jdbcClass + " " + jdbcVar + ";");
    p.prn();

    List<Nf3Field> idFields = nf3Table.fields().stream()
      .filter(Nf3Field::isId)
      .sorted(Comparator.comparing(Nf3Field::idOrder))
      .collect(Collectors.toList());

    p.ofs(1).prn("public " + info.implClassName() + "(" + jdbcClass + " " + jdbcVar + ", " + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaTypeName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");
    p.ofs(2).prn("this." + jdbcVar + " = " + jdbcVar + ";");
    for (Nf3Field f : idFields) {
      p.ofs(2).prn(fIdMap + ".put(\"" + f.dbName() + "\", " + sqlUtilClass + ".fromSql(" + f.javaName() + "));");
    }
    p.ofs(1).prn("}").prn();
  }

  private String generateMainInterface(List<Nf3Table> nf3TableList) {

    if (mainNf36ClassName == null) {
      throw new RuntimeException("Не определён mainNf36ClassName:" +
        " Вызовете метод JavaGenerator.setMainNf36ClassName(...)");
    }

    JavaFilePrinter printer = new JavaFilePrinter();
    printer.packageName = interfaceBasePackage;
    printer.classHeader = "public interface " + mainNf36ClassName;

    for (Nf3Table nf3Table : nf3TableList) {
      printUpsertInterfaceMethod(printer, nf3Table);
    }

    printer.printToFile(UtilsNf36.resolveJavaFile(interfaceOutDir, interfaceBasePackage, mainNf36ClassName));

    return UtilsNf36.resolveFullName(interfaceBasePackage, mainNf36ClassName);
  }


  private void generateMainImpl(List<Nf3Table> nf3TableList, String mainInterfaceClassName) {
    if (mainNf36ImplClassName == null) {
      throw new RuntimeException("Не определён mainNf36ImplClassName:" +
        " Вызовете метод JavaGenerator.setMainNf36ClassName(...)");
    }

    JavaFilePrinter printer = new JavaFilePrinter();
    printer.packageName = implBasePackage;
    printer.classHeader = "public class " + mainNf36ImplClassName
      + " implements " + printer.i(mainInterfaceClassName);

    printJdbcAccessMethod(printer);

    for (Nf3Table nf3Table : nf3TableList) {
      printUpsertImplMethod(printer, nf3Table);
    }

    printer.printToFile(UtilsNf36.resolveJavaFile(implOutDir, implBasePackage, mainNf36ImplClassName));
  }

  private void printJdbcAccessMethod(JavaFilePrinter p) {
    String jdbcClassName = p.i(jdbcDefinition.jdbcClassName());
    String accessMethod = jdbcDefinition.jdbcAccessMethod();
    String notImplError = p.i(NotImplementedException.class.getName());

    p.ofs(1).prn("protected " + jdbcClassName + " " + accessMethod + "() {");
    p.ofs(2).prn("throw new " + notImplError + "();");
    p.ofs(1).prn("}").prn();
  }

  private void printUpsertInterfaceMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpsertInfo upsertInfo = getUpsertInfo(nf3Table);

    p.ofs(1).pr(p.i(upsertInfo.interfaceFullName()))
      .pr(" ").pr(upsertInfo.upsertMethodName()).prn("(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaTypeName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ");").prn();
  }

  private void printUpsertImplMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpsertInfo ui = getUpsertInfo(nf3Table);

    p.ofs(1).prn("@Override");
    p.ofs(1).pr("public ").pr(p.i(ui.interfaceFullName()))
      .pr(" ").pr(ui.upsertMethodName()).prn("(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaTypeName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");

    p.ofs(2).prn("return new " + p.i(ui.implFullName()) + "(" + jdbcDefinition.jdbcAccessMethod() + "(), " + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::javaName)
        .collect(Collectors.joining(", "))

    ) + ");");

    p.ofs(1).prn("}").prn();
  }
}
