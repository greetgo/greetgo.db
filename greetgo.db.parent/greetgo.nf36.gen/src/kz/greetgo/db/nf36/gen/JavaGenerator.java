package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.Nf3CommitMethodName;
import kz.greetgo.db.nf36.core.Nf3MoreMethodName;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kz.greetgo.db.nf36.gen.UtilsNf36.resolveFullName;
import static kz.greetgo.db.nf36.gen.UtilsNf36.resolveJavaFile;

public class JavaGenerator {
  String interfaceOutDir;
  String interfaceBasePackage;

  String implOutDir;
  String implBasePackage;

  String sourceBasePackage;

  String mainNf36ClassName;

  String mainNf36ImplClassName = null;

  private final ModelCollector collector;

  private String mainNf36ImplClassName() {
    if (mainNf36ClassName == null) {
      throw new RuntimeException("Не определён mainNf36ClassName:" +
        " Вызовете метод JavaGenerator.setMainNf36ClassName(...)");
    }
    if (mainNf36ImplClassName != null) return mainNf36ImplClassName;

    return mainNf36ClassAbstract ? "Abstract" + mainNf36ClassName : mainNf36ClassName + "Impl";
  }

  private JavaGenerator(ModelCollector collector) {this.collector = collector;}

  public static JavaGenerator newGenerator(ModelCollector collector) {
    return new JavaGenerator(collector);
  }

  public JavaGenerator setOutDir(String outDir) {
    this.interfaceOutDir = outDir;
    this.implOutDir = outDir;
    return this;
  }

  public JavaGenerator setMainNf36ClassName(String mainNf36ClassName) {
    this.mainNf36ClassName = mainNf36ClassName;
    return this;
  }

  @SuppressWarnings("unused")
  public JavaGenerator setMainNf36ImplClassName(String mainNf36ImplClassName) {
    this.mainNf36ImplClassName = mainNf36ImplClassName;
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

  public void generate() {
    collector.collect();

    if (cleanOutDirsBeforeGeneration) cleanOutDirs();

    String mainInterfaceClassName = generateMainInterface();
    generateMainImpl(mainInterfaceClassName);

    for (Nf3Table nf3Table : collector.collect()) {
      UpsertInfo info = getUpsertInfo(nf3Table);

      generateUpsertInterface(info, nf3Table);
      generateUpsertImpl(info, nf3Table);
    }
  }

  private void cleanOutDirs() {
    UtilsNf36.cleanDir(UtilsNf36.packageDir(implOutDir, implBasePackage));
    UtilsNf36.cleanDir(UtilsNf36.packageDir(interfaceOutDir, interfaceBasePackage));
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
    File interfaceJavaFile = resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = resolveJavaFile(implOutDir, implPackageName, implClassName);

    String upsertMethodName = UtilsNf36.firstToLow(nf3Table.source().getSimpleName());

    String interfaceFullName = resolveFullName(interfacePackageName, interfaceClassName);

    String implFullName = resolveFullName(implPackageName, implClassName);

    Nf3MoreMethodName nf3MoreMethodName = nf3Table.source().getAnnotation(Nf3MoreMethodName.class);

    String moreMethodName = nf3MoreMethodName == null ? collector.moreMethodName() : nf3MoreMethodName.value();

    Nf3CommitMethodName nf3CommitMethodName = nf3Table.source().getAnnotation(Nf3CommitMethodName.class);

    String commitMethodName = nf3CommitMethodName == null ? collector.commitMethodName() : nf3CommitMethodName.value();

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
        return interfaceFullName;
      }

      @Override
      public String implFullName() {
        return implFullName;
      }

      @Override
      public String commitMethodName() {
        return commitMethodName;
      }

      @Override
      public String moreMethodName() {
        return moreMethodName;
      }
    };
  }

  private void generateUpsertInterface(UpsertInfo upsertInfo, Nf3Table nf3Table) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = upsertInfo.interfacePackageName();
    p.classHeader = "public interface " + upsertInfo.interfaceClassName();

    List<Nf3Field> fields = nf3Table.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaType().getName());
      String fieldName = f.javaName();
      p.ofs(1).prn(upsertInfo.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ");").prn();
    }

    p.ofs(1).pr(p.i(upsertInfo.interfaceFullName()))
      .pr(" ").pr(upsertInfo.moreMethodName()).prn("(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ");").prn();

    p.ofs(1).prn("void " + upsertInfo.commitMethodName() + "();");

    p.printToFile(upsertInfo.interfaceJavaFile());
  }

  private void generateUpsertImpl(UpsertInfo upsertInfo, Nf3Table nf3Table) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = upsertInfo.implPackageName();
    String implInterfaceName = p.i(resolveFullName(upsertInfo.interfacePackageName(), upsertInfo.interfaceClassName()));
    p.classHeader = "public class " + upsertInfo.implClassName() + " implements " + implInterfaceName;

    printUpsertImplConstructor(p, upsertInfo, nf3Table);
    printMoreMethodImpl(p, upsertInfo, nf3Table, implInterfaceName);

    List<Nf3Field> fields = nf3Table.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaType().getName());
      String fieldName = f.javaName();
      p.ofs(1).prn("@Override");
      p.ofs(1).prn("public " + upsertInfo.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ") {");

      if (f.notNullAndNotPrimitive()) {
        p.ofs(2).prn("if (" + fieldName + " == null) {");
        p.ofs(3).prn("throw new " + p.i(CannotBeNull.class.getName())
          + "(\"Field " + nf3Table.source().getSimpleName() + "." + f.javaName() + " cannot be null\");");
        p.ofs(2).prn("}");
      }

      p.ofs(2).prn(upserterField + ".putField(\"" + collector.getNf6TableName(nf3Table, f)
        + "\", \"" + f.dbName() + "\", " + fieldName + ");");
      p.ofs(2).prn("return this;");
      p.ofs(1).prn("}").prn();
    }

    printCommitMethodImpl(p, upsertInfo);


    p.printToFile(upsertInfo.implJavaFile());
  }

  private void printCommitMethodImpl(JavaFilePrinter p, UpsertInfo upsertInfo) {
    p.ofs(1).prn("@Override");
    p.ofs(1).prn("public void " + upsertInfo.commitMethodName() + "() {");
    if (collector.nf3ModifiedAtField != null) {
      p.ofs(2).prn(upserterField + ".putUpdateToNowWithParent(\"" + collector.nf3ModifiedAtField + "\");");
    }
    p.ofs(2).prn(upserterField + ".commit();");
    p.ofs(1).prn("}");
  }

  String upserterField = "upserter";

  @SuppressWarnings("unused")
  public JavaGenerator setUpserterField(String upserterField) {
    this.upserterField = upserterField;
    return this;
  }

  private void printUpsertImplConstructor(JavaFilePrinter p, UpsertInfo info, Nf3Table nf3Table) {
    String upserterClassName = p.i(Nf36Upserter.class.getName());

    p.ofs(1).prn("private final " + upserterClassName + " " + upserterField + ";");
    p.prn();

    List<Nf3Field> idFields = nf3Table.fields().stream()
      .filter(Nf3Field::isId)
      .sorted(Comparator.comparing(Nf3Field::idOrder))
      .collect(Collectors.toList());

    Set<String> anotherNames = idFields.stream().map(Nf3Field::javaName).collect(Collectors.toSet());

    String upserterVar = UtilsNf36.selectName(upserterField, anotherNames);

    p.ofs(1).prn("public " + info.implClassName() + "(" + upserterClassName + " " + upserterVar + ", " + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");
    p.ofs(2).prn("this." + upserterField + " = " + upserterVar + ";");
    p.ofs(2).prn(upserterVar + ".setNf3TableName(\"" + nf3Table.nf3TableName() + "\");");
    p.ofs(2).prn(upserterVar + ".setTimeFieldName(\"" + collector.nf6timeField + "\");");

    if (collector.nf3CreatedBy != null) {
      p.ofs(2).prn(upserterVar + ".setAuthorFieldNames("
        + "\"" + collector.nf3CreatedBy.name + "\""
        + ", \"" + collector.nf3ModifiedBy.name + "\""
        + ", \"" + collector.nf6InsertedBy.name + "\""
        + ");");
    }

    p.ofs(2).prn(upserterVar + ".setTimeFieldName(\"" + collector.nf6timeField + "\");");

    for (Nf3Field f : idFields) {
      p.ofs(2).prn(upserterVar + ".putId(\"" + f.dbName() + "\", " + f.javaName() + ");");
    }
    p.ofs(1).prn("}").prn();
  }

  private void printMoreMethodImpl(JavaFilePrinter p, UpsertInfo upsertInfo, Nf3Table nf3Table, String implInterfaceName) {
    p.ofs(1).prn("public " + implInterfaceName + " " + upsertInfo.moreMethodName() + "(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");

    p.ofs(2).prn("return " + upsertInfo.implClassName() + "(this." + upserterField + ".more(), " + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::javaName)
        .collect(Collectors.joining(", "))

    ) + ");");

    p.ofs(1).prn("}").prn();
  }

  private String generateMainInterface() {

    if (mainNf36ClassName == null) {
      throw new RuntimeException("Не определён mainNf36ClassName:" +
        " Вызовете метод JavaGenerator.setMainNf36ClassName(...)");
    }

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = interfaceBasePackage;
    p.classHeader = "public interface " + mainNf36ClassName;

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertInterfaceMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(interfaceOutDir, interfaceBasePackage, mainNf36ClassName));

    return resolveFullName(interfaceBasePackage, mainNf36ClassName);
  }

  private void generateMainImpl(String mainInterfaceClassName) {

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = implBasePackage;
    p.classHeader = "public" + (mainNf36ClassAbstract ? " abstract" : "")
      + " class " + mainNf36ImplClassName()
      + " implements " + p.i(mainInterfaceClassName);

    printCreateUpserterMethod(p);

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertImplMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(implOutDir, implBasePackage, mainNf36ImplClassName()));
  }

  String upserterCreateMethod = "createUpserter";

  @SuppressWarnings("unused")
  public JavaGenerator setUpserterCreateMethod(String upserterCreateMethod) {
    this.upserterCreateMethod = upserterCreateMethod;
    return this;
  }

  boolean mainNf36ClassAbstract = false;

  public JavaGenerator setMainNf36ClassAbstract(boolean mainNf36ClassAbstract) {
    this.mainNf36ClassAbstract = mainNf36ClassAbstract;
    return this;
  }

  private void printCreateUpserterMethod(JavaFilePrinter p) {
    String upserterClassName = p.i(Nf36Upserter.class.getName());
    String notImplError = p.i(RuntimeException.class.getName());

    p.ofs(1).prn("protected " + (mainNf36ClassAbstract ? "abstract " : "")
      + upserterClassName + " " + upserterCreateMethod + "()"
      + (mainNf36ClassAbstract ? ";\n" : " {")
    );

    if (mainNf36ClassAbstract) return;

    p.ofs(2).prn("throw new " + notImplError + "(\"Not implemented\");");
    p.ofs(1).prn("}").prn();
  }

  private void printUpsertInterfaceMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpsertInfo upsertInfo = getUpsertInfo(nf3Table);

    p.ofs(1).pr(p.i(upsertInfo.interfaceFullName()))
      .pr(" ").pr(upsertInfo.upsertMethodName()).prn("(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
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
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");

    p.ofs(2).prn("return new " + p.i(ui.implFullName()) + "(" + upserterCreateMethod + "(), " + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::javaName)
        .collect(Collectors.joining(", "))

    ) + ");");

    p.ofs(1).prn("}").prn();
  }
}
