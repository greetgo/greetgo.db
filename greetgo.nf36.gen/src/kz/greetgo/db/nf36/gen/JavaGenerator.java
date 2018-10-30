package kz.greetgo.db.nf36.gen;

import kz.greetgo.db.nf36.core.Nf36Updater;
import kz.greetgo.db.nf36.core.Nf36Upserter;
import kz.greetgo.db.nf36.core.Nf3CommitMethodName;
import kz.greetgo.db.nf36.core.Nf3MoreMethodName;
import kz.greetgo.db.nf36.core.SequenceNext;
import kz.greetgo.db.nf36.errors.CannotBeNull;
import kz.greetgo.db.nf36.model.Nf3Field;
import kz.greetgo.db.nf36.model.Nf3Table;
import kz.greetgo.db.nf36.model.Sequence;
import kz.greetgo.db.nf36.utils.UtilsNf36;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kz.greetgo.db.nf36.utils.UtilsNf36.firstToLow;
import static kz.greetgo.db.nf36.utils.UtilsNf36.firstToUp;
import static kz.greetgo.db.nf36.utils.UtilsNf36.resolveFullName;
import static kz.greetgo.db.nf36.utils.UtilsNf36.resolveJavaFile;

public class JavaGenerator {
  String interfaceOutDir;
  String interfaceBasePackage;

  String implOutDir;
  String implBasePackage;

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

    if (upserterImplClassName != null) {
      return upserterImplClassName;
    }

    return abstracting ? "Abstract" + upserterClassName : upserterClassName + "Impl";
  }

  private JavaGenerator(ModelCollector collector) {
    this.collector = collector;
  }

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

  public void generate() {
    collector.collect();

    if (cleanOutDirsBeforeGeneration) {
      cleanOutDirs();
    }

    String upserterInterfaceClassName = generateMainUpserterInterface();
    generateMainUpserterImpl(upserterInterfaceClassName);

    if (updaterClassName != null) {
      String updaterInterfaceClassName = generateMainUpdaterInterface();
      generateMainUpdaterImpl(updaterInterfaceClassName);
    }

    for (Nf3Table nf3Table : collector.collect()) {

      {
        UpsertInfo info = getUpsertInfo(nf3Table);
        String baseInterfaceFullName = generateThingUpsertInterface(info);
        generateThingUpsertImpl(info, baseInterfaceFullName);
      }

      if (generateSaver) {
        SaveInfo info = getSaveInfo(nf3Table);
        String baseInterfaceFullName = generateThingSaveInterface(info);
        System.out.println("baseInterfaceFullName = " + baseInterfaceFullName);
      }

      if (updaterClassName != null) {
        UpdateInfo info = getUpdateInfo(nf3Table);
        String baseInterfaceFullName = generateThingUpdateWhereInterface(info);
        generateThingUpdateWhereImpl(info, baseInterfaceFullName);
      }

    }
  }

  private void cleanOutDirs() {
    UtilsNf36.cleanDir(UtilsNf36.packageDir(implOutDir, implBasePackage));
    UtilsNf36.cleanDir(UtilsNf36.packageDir(interfaceOutDir, interfaceBasePackage));
  }

  boolean cleanOutDirsBeforeGeneration = true;

  public JavaGenerator setCleanOutDirsBeforeGeneration(boolean cleanOutDirsBeforeGeneration) {
    this.cleanOutDirsBeforeGeneration = cleanOutDirsBeforeGeneration;
    return this;
  }

  SaveInfo getSaveInfo(Nf3Table nf3Table) {

    String subPackage = UtilsNf36.calcSubPackage(collector.sourceBasePackage, nf3Table.source().getPackage().getName());

    subPackage = UtilsNf36.resolvePackage("save", subPackage);

    String interfaceClassName = nf3Table.source().getSimpleName() + "Save";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = resolveJavaFile(implOutDir, implPackageName, implClassName);

    return new SaveInfo() {
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
      public String implPackageName() {
        return implPackageName;
      }

      @Override
      public String implClassName() {
        return implClassName;
      }

      @Override
      public String saveMethodName() {
        return "save";
      }
    };
  }

  UpsertInfo getUpsertInfo(Nf3Table nf3Table) {

    String subPackage = UtilsNf36.calcSubPackage(collector.sourceBasePackage, nf3Table.source().getPackage().getName());

    subPackage = UtilsNf36.resolvePackage("upsert", subPackage);

    String interfaceClassName = nf3Table.source().getSimpleName() + "Upsert";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = resolveJavaFile(implOutDir, implPackageName, implClassName);

    String upsertMethodName = firstToLow(nf3Table.source().getSimpleName());

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

    String subPackage = UtilsNf36.calcSubPackage(collector.sourceBasePackage, nf3Table.source().getPackage().getName());

    subPackage = UtilsNf36.resolvePackage("update", subPackage);

    String interfaceClassName = nf3Table.source().getSimpleName() + "Update";
    String interfacePackageName = UtilsNf36.resolvePackage(interfaceBasePackage, subPackage);
    File interfaceJavaFile = resolveJavaFile(interfaceOutDir, interfacePackageName, interfaceClassName);

    String implClassName = interfaceClassName + "Impl";
    String implPackageName = UtilsNf36.resolvePackage(implBasePackage, subPackage);
    File implJavaFile = resolveJavaFile(implOutDir, implPackageName, implClassName);

    String interfaceFullName = resolveFullName(interfacePackageName, interfaceClassName);

    String updateMethodName = firstToLow(nf3Table.source().getSimpleName());

    String implFullName = resolveFullName(implPackageName, implClassName);

    Nf3CommitMethodName nf3CommitMethodName = nf3Table.source().getAnnotation(Nf3CommitMethodName.class);

    String commitMethodName = nf3CommitMethodName == null ? collector.commitMethodName() : nf3CommitMethodName.value();

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
        return "where" + firstToUp(f.javaName()) + "IsEqualTo";
      }

      @Override
      public String setMethodName(Nf3Field f) {
        return "set" + firstToUp(f.javaName());
      }

      @Override
      public String implFullName() {
        return implFullName;
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

      @Override
      public String commitMethodName() {
        return commitMethodName;
      }
    };
  }

  private String generateThingSaveInterface(SaveInfo info) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.interfacePackageName();
    p.classHeader = "public interface " + info.interfaceClassName();

    p.ofs(1).prn("void " + info.saveMethodName() + "();");

    p.printToFile(info.interfaceJavaFile());

    return resolveFullName(info.interfacePackageName(), info.interfaceClassName());
  }

  private String generateThingUpsertInterface(UpsertInfo info) {
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

  private String generateThingUpdateWhereInterface(UpdateInfo info) {
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
        p.ofs(1).prn(info.interfaceClassName() + " " + info.setMethodName(f)
            + "(" + fieldType + " " + f.javaName() + ");").prn();
      }
    }

    p.prn();

    {
      List<Nf3Field> fields = info.fields().stream()
          .sorted(Comparator.comparing(Nf3Field::javaName))
          .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        p.ofs(1).prn(info.interfaceClassName() + " " + info.whereMethodName(f)
            + "(" + fieldType + " " + f.javaName() + ");").prn();
      }
    }

    p.ofs(1).prn("void " + info.commitMethodName() + "();");

    p.printToFile(info.interfaceJavaFile());

    return resolveFullName(info.interfacePackageName(), info.interfaceClassName());
  }

  private void generateThingUpsertImpl(UpsertInfo info, String baseInterfaceFullName) {
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

  private void generateThingUpdateWhereImpl(UpdateInfo info, String baseInterfaceFullName) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = info.implPackageName();
    String implInterfaceName = p.i(baseInterfaceFullName);
    p.classHeader = "public class " + info.implClassName() + " implements " + implInterfaceName;

    printUpdaterWhereImplConstructor(info, p);

    {
      List<Nf3Field> fields = info.fields().stream()
          .filter(f -> !f.isId())
          .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        p.ofs(1).prn("@Override");
        p.ofs(1).prn("public " + info.interfaceClassName() + " " + info.setMethodName(f)
            + "(" + fieldType + " " + f.javaName() + ") {");
        p.ofs(2).prn("this.updater.setField(\"" + info.nf6TableName(f) + "\", \"" + f.dbName() + "\", " + f.javaName() + ");");
        p.ofs(2).prn("return this;");
        p.ofs(1).prn("}").prn();
      }
    }

    p.prn().prn();

    {
      List<Nf3Field> fields = info.fields().stream()
          .sorted(Comparator.comparing(Nf3Field::javaName))
          .collect(Collectors.toList());

      for (Nf3Field f : fields) {
        String fieldType = p.i(f.javaType().getName());
        p.ofs(1).prn("@Override");
        p.ofs(1).prn("public " + info.interfaceClassName() + " " + info.whereMethodName(f)
            + "(" + fieldType + " " + f.javaName() + ") {");

        if (f.notNullAndNotPrimitive()) {
          p.ofs(2).prn("if (" + f.javaName() + " == null) {");
          p.ofs(3).prn("throw new " + p.i(CannotBeNull.class.getName())
              + "(\"Field " + info.source().getSimpleName() + "." + f.javaName() + " cannot be null\");");
          p.ofs(2).prn("}");
        }

        p.ofs(2).prn("this.updater.where(\"" + f.dbName() + "\", " + f.javaName() + ");");
        p.ofs(2).prn("return this;");
        p.ofs(1).prn("}").prn();
      }
    }

    {
      p.ofs(1).prn("@Override");
      p.ofs(1).prn("public void " + info.commitMethodName() + "() {");
      p.ofs(2).prn("this.updater.commit();");
      p.ofs(1).prn("}");
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

  private void printUpdaterWhereImplConstructor(UpdateInfo info, JavaFilePrinter p) {
    String nameNf36Updater = p.i(Nf36Updater.class.getName());

    p.ofs(1).prn("private final " + nameNf36Updater + " updater;").prn();
    p.ofs(1).prn("public " + info.implClassName() + "(" + nameNf36Updater + " updater) {");
    p.ofs(2).prn("this.updater = updater;");

    p.ofs(2).prn("updater.setNf3TableName(\"" + info.nf3TableName() + "\");");

    if (collector.nf3ModifiedBy != null) {
      p.ofs(2).prn("updater.setAuthorFieldNames("
          + "\"" + collector.nf3ModifiedBy.name + "\""
          + ", \"" + collector.nf6InsertedBy.name + "\""
          + ");");
    }

    if (collector.nf3ModifiedAtField != null) {
      p.ofs(2).prn("updater.updateFieldToNow(\"" + collector.nf3ModifiedAtField + "\");");
    }

    p.ofs(2).prn("updater.setIdFieldNames(" + (
        info.fields().stream()
            .filter(Nf3Field::isId)
            .sorted(Comparator.comparing(Nf3Field::idOrder))
            .map(f -> "\"" + f.dbName() + "\"")
            .collect(Collectors.joining(", "))
    ) + ");");

    p.ofs(1).prn("}").prn();
  }

  private void printMoreMethodImpl(JavaFilePrinter p, UpsertInfo info, String implInterfaceName) {
    p.ofs(1).prn("@Override");
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

  private String generateMainUpserterInterface() {

    if (upserterClassName == null) {
      throw new RuntimeException("Не определён upserterClassName:" +
          " Вызовете метод JavaGenerator.setUpserterClassName(...)");
    }

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = interfaceBasePackage;
    p.classHeader = "public interface " + upserterClassName;

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertInterfaceMethod(p, nf3Table);
      for (Nf3Field nf3Field : nf3Table.fields()) {
        if (nf3Field.sequence() != null) {
          printUpsertInterfaceMethodSequence(p, nf3Field, nf3Table);
        }
      }
    }

    p.printToFile(resolveJavaFile(interfaceOutDir, interfaceBasePackage, upserterClassName));

    return resolveFullName(interfaceBasePackage, upserterClassName);
  }


  private String generateMainUpdaterInterface() {

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = interfaceBasePackage;
    p.classHeader = "public interface " + updaterClassName;

    for (Nf3Table nf3Table : collector.collect()) {
      printUpdateInterfaceMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(interfaceOutDir, interfaceBasePackage, updaterClassName));

    return resolveFullName(interfaceBasePackage, updaterClassName);
  }

  private void generateMainUpserterImpl(String upserterInterfaceClassName) {

    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = implBasePackage;
    p.classHeader = "public" + (abstracting ? " abstract" : "")
        + " class " + upserterImplClassName()
        + " implements " + p.i(upserterInterfaceClassName);

    printCreateUpserterMethod(p);
    printGetSequenceNextMethod(p);

    for (Nf3Table nf3Table : collector.collect()) {
      printUpsertImplMethod(p, nf3Table);
      for (Nf3Field nf3Field : nf3Table.fields()) {
        if (nf3Field.sequence() != null) {
          printUpsertImplMethodSequence(p, nf3Field, nf3Table);
        }
      }
    }

    p.printToFile(resolveJavaFile(implOutDir, implBasePackage, upserterImplClassName()));
  }


  private void generateMainUpdaterImpl(String updaterInterfaceClassName) {
    JavaFilePrinter p = new JavaFilePrinter();
    p.packageName = implBasePackage;
    p.classHeader = "public" + (abstracting ? " abstract" : "")
        + " class " + updaterImplClassName()
        + " implements " + p.i(updaterInterfaceClassName);

    printCreateUpdaterMethod(p);

    for (Nf3Table nf3Table : collector.collect()) {
      printUpdateImplMethod(p, nf3Table);
    }

    p.printToFile(resolveJavaFile(implOutDir, implBasePackage, updaterImplClassName()));
  }

  String upserterCreateMethod = "createUpserter";

  @SuppressWarnings("unused")
  public JavaGenerator setUpserterCreateMethod(String upserterCreateMethod) {
    this.upserterCreateMethod = upserterCreateMethod;
    return this;
  }

  String getSequenceNextMethod = "getSequenceNext";

  @SuppressWarnings("unused")
  public JavaGenerator setGetSequenceNextMethod(String getSequenceNextMethod) {
    this.getSequenceNextMethod = getSequenceNextMethod;
    return this;
  }

  String updaterCreateMethod = "createUpdater";

  @SuppressWarnings("unused")
  public JavaGenerator setUpdaterCreateMethod(String updaterCreateMethod) {
    this.updaterCreateMethod = updaterCreateMethod;
    return this;
  }

  boolean abstracting = false;

  public JavaGenerator setAbstracting(boolean abstracting) {
    this.abstracting = abstracting;
    return this;
  }

  boolean generateSaver = false;

  public JavaGenerator setGenerateSaver(boolean generateSaver) {
    this.generateSaver = generateSaver;
    return this;
  }

  private void printCreateUpserterMethod(JavaFilePrinter p) {
    String upserterClassName = p.i(Nf36Upserter.class.getName());

    p.ofs(1).prn("protected " + (abstracting ? "abstract " : "")
        + upserterClassName + " " + upserterCreateMethod + "()"
        + (abstracting ? ";\n" : " {")
    );

    if (abstracting) { return; }

    String notImplError = p.i(RuntimeException.class.getName());

    p.ofs(2).prn("throw new " + notImplError + "(\"Not implemented\");");
    p.ofs(1).prn("}").prn();
  }

  private void printGetSequenceNextMethod(JavaFilePrinter p) {
    String sequenceNextClassName = p.i(SequenceNext.class.getName());

    p.ofs(1).prn("protected " + (abstracting ? "abstract " : "")
        + sequenceNextClassName + " " + getSequenceNextMethod + "()"
        + (abstracting ? ";\n" : " {")
    );

    if (abstracting) { return; }

    String notImplError = p.i(RuntimeException.class.getName());

    p.ofs(2).prn("throw new " + notImplError + "(\"Not implemented\");");
    p.ofs(1).prn("}").prn();
  }

  private void printCreateUpdaterMethod(JavaFilePrinter p) {
    String updaterClassName = p.i(Nf36Updater.class.getName());

    p.ofs(1).prn("protected " + (abstracting ? "abstract " : "")
        + updaterClassName + " " + updaterCreateMethod + "()"
        + (abstracting ? ";\n" : " {")
    );

    if (abstracting) { return; }

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

  private void printUpsertInterfaceMethodSequence(JavaFilePrinter p, Nf3Field nf3Field, Nf3Table nf3Table) {
    UpsertInfo info = getUpsertInfo(nf3Table);

    p.ofs(1).pr(p.i(nf3Field.javaType().getName()))
        .pr(" ").pr(info.upsertMethodName() + "Next" + firstToUp(nf3Field.javaName())).prn("();").prn();
  }

  private void printUpsertImplMethodSequence(JavaFilePrinter p, Nf3Field nf3Field, Nf3Table nf3Table) {
    UpsertInfo info = getUpsertInfo(nf3Table);

    Sequence sequence = nf3Field.sequence();

    p.ofs(1).prn("@Override");
    p.ofs(1).pr("public " + p.i(nf3Field.javaType().getName()))
        .pr(" ").pr(info.upsertMethodName() + "Next" + firstToUp(nf3Field.javaName())).prn("() {");
    p.ofs(2).prn("return " + getSequenceNextMethod + "().next"
        + firstToUp(nf3Field.javaType().getSimpleName()) + "(\"" + sequence.name + "\");");
    p.ofs(1).prn("}").prn();
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

  private void printUpdateImplMethod(JavaFilePrinter p, Nf3Table nf3Table) {
    UpdateInfo info = getUpdateInfo(nf3Table);

    p.ofs(1).prn("@Override");
    p.ofs(1).pr("public ").pr(p.i(info.interfaceFullName())).pr(" ").pr(info.updateMethodName()).prn("() {");
    p.ofs(2).prn("return new " + p.i(info.implFullName()) + "(" + updaterCreateMethod + "());");
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
    if (updaterImplClassName != null) { return updaterImplClassName; }
    return abstracting ? "Abstract" + updaterClassName : updaterClassName + "Impl";
  }
}
