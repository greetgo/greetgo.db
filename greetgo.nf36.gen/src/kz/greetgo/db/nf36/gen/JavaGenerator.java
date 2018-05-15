package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.Nf3CommitMethodName;
import kz.greetgo.db.nf36.core.Nf3MoreMethodName;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.utils.UtilsNf36;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kz.greetgo.db.nf36.utils.UtilsNf36.firstToUp;
import static kz.greetgo.db.nf36.utils.UtilsNf36.resolveFullName;
import static kz.greetgo.db.nf36.utils.UtilsNf36.resolveJavaFile;

public class JavaGenerator {
  String interfaceOutDir;
  String interfaceBasePackage;

  String implOutDir;
  String implBasePackage;

  String sourceBasePackage;

  String upserterClassName;

  String upserterImplClassName = null;

  String updaterClassName = null;

  String updaterImplClassName = null;

  private final ModelCollector collector;


  private String upserterImplClassName() {
    if (upserterClassName == null) {
      throw new RuntimeException("Не определён upserterClassName:" +
        " Вызовете метод JavaGenerator.setUpserterClassName(...)");
    }
    if (upserterImplClassName != null) return upserterImplClassName;

    return abstracting ? "Abstract" + upserterClassName : upserterClassName + "Impl";
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

  public JavaGenerator setUpserterClassName(String upserterClassName) {
    this.upserterClassName = upserterClassName;
    return this;
  }

  @SuppressWarnings("unused")
  public JavaGenerator setUpserterImplClassName(String upserterImplClassName) {
    this.upserterImplClassName = upserterImplClassName;
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

  //TODO public void generate()
  public void generate() {
    collector.collect();

    if (cleanOutDirsBeforeGeneration) cleanOutDirs();

    String upserterInterfaceClassName = generateUpserterInterface();
    generateUpserterImpl(upserterInterfaceClassName);

    if (updaterClassName != null) {
      String updaterInterfaceClassName = generateUpdaterInterface();
      generateUpdaterImpl(updaterInterfaceClassName);
    }

    for (Nf3Table nf3Table : collector.collect()) {

      {
        UpsertInfo info = getUpsertInfo(nf3Table);
        String baseInterfaceFullName = generateUpsertInterface(info);
        generateUpsertImpl(info, baseInterfaceFullName);
      }

      if (updaterClassName != null) {
        UpdateInfo info = getUpdateInfo(nf3Table);
        String baseInterfaceFullName = generateUpdateInterface(info);
        generateUpdateImpl(info, baseInterfaceFullName);
      }

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

      @Override
      public List<Nf3Field> fields() {
        return nf3Table.fields();
      }

      @Override
      public String nf3TableName() {
        return nf3Table.nf3TableName();
      }

      @Override
      public Class<?> source() {
        return nf3Table.source();
      }

      @Override
      public String nf6TableName(Nf3Field f) {
        return nf3Table.nf6prefix() + nf3Table.tableName() + collector.nf6TableSeparator + f.rootField().dbName();
      }
    };
  }

  UpdateInfo getUpdateInfo(Nf3Table nf3Table) {

    String subPackage = UtilsNf36.calcSubPackage(sourceBasePackage, nf3Table.source().getPackage().getName());

    subPackage = UtilsNf36.resolvePackage("update", subPackage);

    String interfaceClassName = nf3Table.source().getSimpleName() + "Update";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = resolveJavaFile(implOutDir, implPackageName, implClassName);

    String interfaceFullName = resolveFullName(interfacePackageName, interfaceClassName);

    String updateMethodName = UtilsNf36.firstToLow(nf3Table.source().getSimpleName());

    return new UpdateInfo() {
      @Override
      public String interfaceClassName() {
        return interfaceClassName;
      }

      @Override
      public String interfacePackageName() {
        return interfacePackageName;
      }

      @Override
      public File interfaceJavaFile() {
        return interfaceJavaFile;
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
      public File implJavaFile() {
        return implJavaFile;
      }

      @Override
      public List<Nf3Field> fields() {
        return nf3Table.fields();
      }

      @Override
      public String interfaceFullName() {
        return interfaceFullName;
      }

      @Override
      public String updateMethodName() {
        return updateMethodName;
      }

      @Override
      public String whereMethodName(Nf3Field f) {
        return "where" + firstToUp(f.javaName()) + "EqualTo";
      }
    };
  }

  private String generateUpsertInterface(UpsertInfo info) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.interfacePackageName();
    p.classHeader = "public interface " + info.interfaceClassName();

    List<Nf3Field> fields = info.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaType().getName());
      String fieldName = f.javaName();
      p.ofs(1).prn(info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ");").prn();
    }

    p.ofs(1).pr(p.i(info.interfaceFullName()))
      .pr(" ").pr(info.moreMethodName()).prn("(" + (

      info.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ");").prn();

    p.ofs(1).prn("void " + info.commitMethodName() + "();");

    p.printToFile(info.interfaceJavaFile());

    return resolveFullName(info.interfacePackageName(), info.interfaceClassName());
  }

  private String generateUpdateInterface(UpdateInfo info) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.interfacePackageName();
    p.classHeader = "public interface " + info.interfaceClassName();

    {
      List<Nf3Field> fields = info.fields().stream()
        .filter(f -> !f.isId())
        .sorted(Comparator.comparing(Nf3Field::javaName))
        .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        String fieldName = f.javaName();
        p.ofs(1).prn(info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ");");
      }
    }

    {
      List<Nf3Field> fields = info.fields().stream()
        .filter(f -> !f.isId())
        .sorted(Comparator.comparing(Nf3Field::javaName))
        .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        p.ofs(1).prn(info.interfaceClassName() + " " + info.whereMethodName(f) + "(" + fieldType + " " + f.javaName() + ");");
      }
    }

    p.printToFile(info.interfaceJavaFile());

    return resolveFullName(info.interfacePackageName(), info.interfaceClassName());
  }

  private void generateUpsertImpl(UpsertInfo info, String baseInterfaceFullName) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.implPackageName();
    String implInterfaceName = p.i(baseInterfaceFullName);
    p.classHeader = "public class " + info.implClassName() + " implements " + implInterfaceName;

    printUpsertImplConstructor(p, info);
    printMoreMethodImpl(p, info, implInterfaceName);

    List<Nf3Field> fields = info.fields().stream()
      .filter(f -> !f.isId())
      .collect(Collectors.toList());

    for (Nf3Field f : fields) {
      String fieldType = p.i(f.javaType().getName());
      String fieldName = f.javaName();
      p.ofs(1).prn("@Override");
      p.ofs(1).prn("public " + info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ") {");

      if (f.notNullAndNotPrimitive()) {
        p.ofs(2).prn("if (" + fieldName + " == null) {");
        p.ofs(3).prn("throw new " + p.i(CannotBeNull.class.getName())
          + "(\"Field " + info.source().getSimpleName() + "." + f.javaName() + " cannot be null\");");
        p.ofs(2).prn("}");
      }

      p.ofs(2).prn(upserterField + ".putField(\"" + info.nf6TableName(f) + "\", \""
        + f.dbName() + "\", " + fieldName + ");");
      p.ofs(2).prn("return this;");
      p.ofs(1).prn("}").prn();
    }

    printCommitMethodImpl(p, info);

    p.printToFile(info.implJavaFile());
  }

  private void generateUpdateImpl(UpdateInfo info, String baseInterfaceFullName) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.implPackageName();
    String implInterfaceName = p.i(baseInterfaceFullName);
    p.classHeader = "public class " + info.implClassName() + " implements " + implInterfaceName;

    {
      List<Nf3Field> fields = info.fields().stream()
        .filter(f -> !f.isId())
        .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        String fieldName = f.javaName();
        p.ofs(1).prn("@Override");
        p.ofs(1).prn("public " + info.interfaceClassName() + " " + fieldName + "(" + fieldType + " " + fieldName + ") {");
        p.ofs(2).prn("return this;");
        p.ofs(1).prn("}").prn();
      }
    }

    {
      List<Nf3Field> fields = info.fields().stream()
        .filter(f -> !f.isId())
        .sorted(Comparator.comparing(Nf3Field::javaName))
        .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        String fieldName = f.javaName();
        p.ofs(1).prn("@Override");
        p.ofs(1).prn("public " + info.interfaceClassName() + " " + info.whereMethodName(f) + "(" + fieldType + " " + fieldName + ") {");
        p.ofs(2).prn("return this;");
        p.ofs(1).prn("}").prn();
      }
    }

    p.printToFile(info.implJavaFile());
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

  private void printUpsertImplConstructor(JavaFilePrinter p, UpsertInfo info) {
    String upserterClassName = p.i(Nf36Upserter.class.getName());

    p.ofs(1).prn("private final " + upserterClassName + " " + upserterField + ";");
    p.prn();

    List<Nf3Field> idFields = info.fields().stream()
      .filter(Nf3Field::isId)
      .sorted(Comparator.comparing(Nf3Field::idOrder))
      .collect(Collectors.toList());

    Set<String> anotherNames = idFields.stream().map(Nf3Field::javaName).collect(Collectors.toSet());

    String upserterVar = UtilsNf36.selectName(upserterField, anotherNames);

    p.ofs(1).prn("public " + info.implClassName() + "(" + upserterClassName + " " + upserterVar + ", " + (

      info.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");
    p.ofs(2).prn("this." + upserterField + " = " + upserterVar + ";");
    p.ofs(2).prn(upserterVar + ".setNf3TableName(\"" + info.nf3TableName() + "\");");
    p.ofs(2).prn(upserterVar + ".setTimeFieldName(\"" + collector.nf6timeField + "\");");

    if (collector.nf3CreatedBy != null) {
      p.ofs(2).prn(upserterVar + ".setAuthorFieldNames("
        + "\"" + collector.nf3CreatedBy.name + "\""
        + ", \"" + collector.nf3ModifiedBy.name + "\""
        + ", \"" + collector.nf6InsertedBy.name + "\""
        + ");");
    }

    for (Nf3Field f : idFields) {
      p.ofs(2).prn(upserterVar + ".putId(\"" + f.dbName() + "\", " + f.javaName() + ");");
    }
    p.ofs(1).prn("}").prn();
  }

  private void printMoreMethodImpl(JavaFilePrinter p, UpsertInfo info, String implInterfaceName) {
    p.ofs(1).prn("public " + implInterfaceName + " " + info.moreMethodName() + "(" + (

      info.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ") {");

    p.ofs(2).prn("return new " + info.implClassName() + "(this." + upserterField + ".more(), " + (

      info.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(Nf3Field::javaName)
        .collect(Collectors.joining(", "))

    ) + ");");

    p.ofs(1).prn("}").prn();
  }

  private String generateUpserterInterface() {

    if (upserterClassName == null) {
      throw new RuntimeException("Не определён upserterClassName:" +
        " Вызовете метод JavaGenerator.setUpserterClassName(...)");
    }

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = interfaceBasePackage;
    p.classHeader = "public interface " + upserterClassName;

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertInterfaceMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(interfaceOutDir, interfaceBasePackage, upserterClassName));

    return resolveFullName(interfaceBasePackage, upserterClassName);
  }

  private String generateUpdaterInterface() {

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = interfaceBasePackage;
    p.classHeader = "public interface " + updaterClassName;

    for (Nf3Table nf3Table : collector.collect()) {
      printUpdateInterfaceMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(interfaceOutDir, interfaceBasePackage, updaterClassName));

    return resolveFullName(interfaceBasePackage, updaterClassName);
  }

  private void generateUpserterImpl(String upserterInterfaceClassName) {

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = implBasePackage;
    p.classHeader = "public" + (abstracting ? " abstract" : "")
      + " class " + upserterImplClassName()
      + " implements " + p.i(upserterInterfaceClassName);

    printCreateUpserterMethod(p);

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertImplMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(implOutDir, implBasePackage, upserterImplClassName()));
  }

  private void generateUpdaterImpl(String updaterInterfaceClassName) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = implBasePackage;
    p.classHeader = "public" + (abstracting ? " abstract" : "")
      + " class " + updaterImplClassName()
      + " implements " + p.i(updaterInterfaceClassName);

    p.printToFile(resolveJavaFile(implOutDir, implBasePackage, updaterImplClassName()));
  }

  String upserterCreateMethod = "createUpserter";

  @SuppressWarnings("unused")
  public JavaGenerator setUpserterCreateMethod(String upserterCreateMethod) {
    this.upserterCreateMethod = upserterCreateMethod;
    return this;
  }

  boolean abstracting = false;

  public JavaGenerator setAbstracting(boolean abstracting) {
    this.abstracting = abstracting;
    return this;
  }

  private void printCreateUpserterMethod(JavaFilePrinter p) {
    String upserterClassName = p.i(Nf36Upserter.class.getName());

    p.ofs(1).prn("protected " + (abstracting ? "abstract " : "")
      + upserterClassName + " " + upserterCreateMethod + "()"
      + (abstracting ? ";\n" : " {")
    );

    if (abstracting) return;

    String notImplError = p.i(RuntimeException.class.getName());

    p.ofs(2).prn("throw new " + notImplError + "(\"Not implemented\");");
    p.ofs(1).prn("}").prn();
  }

  private void printUpsertInterfaceMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpsertInfo info = getUpsertInfo(nf3Table);

    p.ofs(1).pr(p.i(info.interfaceFullName()))
      .pr(" ").pr(info.upsertMethodName()).prn("(" + (

      nf3Table.fields().stream()
        .filter(Nf3Field::isId)
        .sorted(Comparator.comparing(Nf3Field::idOrder))
        .map(f -> p.i(f.javaType().getName()) + " " + f.javaName())
        .collect(Collectors.joining(", "))

    ) + ");").prn();
  }

  private void printUpdateInterfaceMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpdateInfo info = getUpdateInfo(nf3Table);

    p.ofs(1).pr(p.i(info.interfaceFullName())).pr(" ").pr(info.updateMethodName()).prn("();").prn();
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

  public JavaGenerator setUpdaterClassName(String updaterClassName) {
    this.updaterClassName = updaterClassName;
    return this;
  }

  @SuppressWarnings("unused")
  public JavaGenerator setUpdaterImplClassName(String updaterImplClassName) {
    this.updaterImplClassName = updaterImplClassName;
    return this;
  }

  private String updaterImplClassName() {
    if (updaterClassName == null) {
      throw new RuntimeException("Если updaterClassName == null, то updateWhere генерироваться не должен");
    }
    if (updaterImplClassName != null) return updaterImplClassName;
    return abstracting ? "Abstract" + updaterClassName : updaterClassName + "Impl";
  }
}
