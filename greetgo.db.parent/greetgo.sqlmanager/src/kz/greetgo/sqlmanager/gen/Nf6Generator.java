package kz.greetgo.sqlmanager.gen;

import static kz.greetgo.util.ServerUtil.firstUpper;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kz.greetgo.sqlmanager.model.EnumType;
import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.FieldDb;
import kz.greetgo.sqlmanager.model.SimpleType;
import kz.greetgo.sqlmanager.model.Table;
import kz.greetgo.sqlmanager.model.Type;
import kz.greetgo.sqlmanager.model.command.Command;
import kz.greetgo.sqlmanager.model.command.SelectAll;
import kz.greetgo.sqlmanager.model.command.ToDictionary;
import kz.greetgo.sqlmanager.parser.StruParseException;
import kz.greetgo.sqlmanager.parser.StruShaper;

/**
 * Содержит основную логику генерации
 * 
 * <p>
 * Класс абстрактный, потому что для различных БД генерируемый код может отличатся. Но пока основной
 * генерируемый код расположен здесь</i>
 * </p>
 * 
 * @author pompei
 * 
 */
public abstract class Nf6Generator {
  
  /**
   * Тип внешней библиотеки
   * 
   * <p>
   * Инициируется значением {@link LibType.MYBATIS} для обратной совместимости. <i>Для того, чтобы
   * старый код, которые был написан для MyBatis, ещё до введения типа {@link LibType}, работал бы
   * без ошибок</i>
   * </p>
   */
  public LibType libType = LibType.MYBATIS;
  
  public static final String MYBATIS_StatementType = "org.apache.ibatis.mapping.StatementType";
  public static final String MYBATIS_OPTIONS = "org.apache.ibatis.annotations.Options";
  public static final String MYBATIS_SELECT = "org.apache.ibatis.annotations.Select";
  public static final String MYBATIS_UPDATE = "org.apache.ibatis.annotations.Update";
  public static final String MYBATIS_DELETE = "org.apache.ibatis.annotations.Delete";
  public static final String MYBATIS_INSERT = "org.apache.ibatis.annotations.Insert";
  public static final String MYBATIS_PARAM = "org.apache.ibatis.annotations.Param";
  
  public static final String GBATIS_PRM = "kz.greetgo.gbatis.t.Prm";
  public static final String GBATIS_SELE = "kz.greetgo.gbatis.t.Sele";
  public static final String GBATIS_CALL = "kz.greetgo.gbatis.t.Call";
  public static final String GBATIS_AUTOIMPL = "kz.greetgo.gbatis.t.Autoimpl";
  
  public static final String LIST = "java.util.List";
  public static final String DATE = "java.util.Date";
  public static final String OBJECTS = "java.util.Objects";
  public static final String ARRAYS = "java.util.Arrays";
  
  /**
   * Содержит DOM
   */
  private final StruShaper sg;
  
  /**
   * Получет диалект для рабочей БД
   * 
   * @return Получает {@link SqlDialect}
   */
  protected abstract SqlDialect sqld();
  
  /**
   * Содежрит конфигурацию генератора
   */
  public final Conf conf;
  
  /**
   * Ссылка на формирователь вьюшек, для доступа к данным
   */
  protected final ViewFormer viewFormer;
  
  /**
   * Основной конструктор
   * 
   * @param conf
   *          конфигурация генерации
   * @param sg
   *          подготовленная структура
   */
  public Nf6Generator(Conf conf, StruShaper sg) {
    this.conf = conf;
    this.sg = sg;
    this.viewFormer = new ViewFormerRowNumber(conf);
  }
  
  /**
   * Распечатка SQL-ей, для копирования данных из исторических таблиц в оперативные
   * 
   * @param out
   *          место вывода SQL-ей
   */
  public void printHistToOperSqls(PrintStream out, String separator) {
    if (separator == null) separator = conf.separator;
    
    List<String> tnames = getTnames();
    
    for (String tname : tnames) {
      Table table = sg.stru.tables.get(tname);
      for (Field field : table.fields) {
        out.println(separator.replaceAll("NAME", tname + "_" + field.name));
        printHistToOperField(out, field);
      }
    }
  }
  
  private void printHistToOperField(PrintStream out, Field field) {
    String mname = conf.tabPrefix + field.table.name + "_" + field.name;
    String oname = conf.oPref + field.table.name + "_" + field.name;
    
    String ins = conf.insertedAt;
    String modi = conf.lastModifiedAt;
    
    final String keys;
    {
      StringBuilder sb = new StringBuilder();
      for (Field key : field.table.keys) {
        for (FieldDb dbKey : key.dbFields()) {
          sb.append(dbKey.name).append(", ");
        }
      }
      sb.setLength(sb.length() - 2);
      keys = sb.toString();
    }
    
    final String fields;
    {
      StringBuilder sb = new StringBuilder();
      for (FieldDb dbField : field.dbFields()) {
        sb.append(dbField.name).append(", ");
      }
      sb.setLength(sb.length() - 2);
      fields = sb.toString();
    }
    
    String all = keys + ", " + fields;
    String copy = "  " + all;
    if (ins != null) copy += ", " + ins;
    if (modi != null) copy += ", " + modi;
    
    String ts = conf.ts;
    
    out.println("insert into " + oname + '(');
    out.println(copy);
    out.println(") select");
    out.println(copy);
    out.println("from");
    out.println('(');
    out.println("  select distinct " + keys + " from " + mname);
    out.println("  " + minus());
    out.println("  select " + keys + " from " + oname);
    out.println(") x1");
    out.println("natural join");
    out.println('(');
    out.print("  select " + all);
    if (modi != null) out.print(", " + ts + " as " + modi);
    out.println(',');
    out.println("    row_number() over (partition by " + keys + " order by " + ts
        + " desc) as modi_nom");
    out.println("  from " + mname);
    out.println(") x2");
    if (ins != null) {
      out.println("natural join");
      out.println('(');
      
      out.println("  select " + keys + ", " + ts + " as " + ins + ',');
      out.println("    row_number() over (partition by " + keys + " order by " + ts
          + " asc) as ins_nom");
      out.println("  from " + mname);
      out.println(") x3");
    }
    out.println("where modi_nom = 1");
    if (ins != null) out.println("and ins_nom = 1");
  }
  
  protected abstract String minus();
  
  /**
   * Распечатка SQL-ей для генерации структуры данных (таблицы, секвенсы, вьюшки)
   * 
   * @param out
   *          место вывода SQL-ей
   */
  public void printSqls(PrintStream out) {
    
    printPrepareSqls(out);
    
    List<String> tnames = getTnames();
    
    for (String name : tnames) {
      Table table = sg.stru.tables.get(name);
      printKeyTable(table, out);
      for (Field field : table.fields) {
        printFieldsTable(field, out);
        if (conf.genOperTables) {
          printFieldsOperTable(field, out);
        }
      }
      out.println();
    }
    
    for (String name : tnames) {
      Table table = sg.stru.tables.get(name);
      if (table.keys.size() == 1) {
        Type keyType = table.keys.get(0).type;
        if (keyType instanceof SimpleType && ((SimpleType)keyType).needSequence) {
          String from = "";
          if (table.sequenceFrom > 0) {
            from = " start with " + table.sequenceFrom;
          }
          out.println("create sequence " + conf.seqPrefix + table.name + from + conf.separator);
        }
      }
    }
    
    out.println();
    for (String tname : tnames) {
      Table table = sg.stru.tables.get(tname);
      printPrimaryForeignKey(table, out);
      for (Field field : table.fields) {
        if (field.type instanceof Table) {
          List<SimpleType> types = new ArrayList<>();
          field.type.assignSimpleTypes(types);
          List<String> fieldNames = new ArrayList<>();
          if (types.size() == 1) {
            fieldNames.add(field.name);
          } else
            for (int i = 1, C = types.size(); i <= C; i++) {
              fieldNames.add(field.name + i);
            }
          out.println("alter table " + conf.tabPrefix + tname + "_" + field.name + " add "
              + formForeignKey(fieldNames, (Table)field.type) + conf.separator);
          if (conf.genOperTables) {
            out.println("alter table " + conf.oPref + tname + "_" + field.name + " add "
                + formForeignKey(fieldNames, (Table)field.type) + conf.separator);
          }
        }
      }
    }
    out.println();
    
    printViews(tnames, out);
  }
  
  /**
   * Распечатка SQL-ей для генерации хранимой логики (хранимые процедуры и функции)
   * 
   * @param out
   *          место вывода SQL-ей
   */
  public void printPrograms(PrintStream out) {
    List<String> tnames = getTnames();
    printInsertFunctions(tnames, out);
  }
  
  private List<String> getTnames() {
    List<String> tnames = new ArrayList<>();
    tnames.addAll(sg.stru.tables.keySet());
    Collections.sort(tnames);
    return tnames;
  }
  
  protected abstract void printPrepareSqls(PrintStream out);
  
  private void printPrimaryForeignKey(Table table, PrintStream out) {
    for (Field field : table.keys) {
      if (field.type instanceof Table) {
        List<Field> keys = ((Table)field.type).keys;
        out.print("alter table " + conf.tabPrefix + table.name + " add foreign key (");
        if (keys.size() == 1) {
          out.print(field.name);
        } else
          for (int i = 0, C = keys.size(); i < C; i++) {
            out.print(i == 0 ? "" :", ");
            out.print(field.name + (i + 1));
          }
        out.print(") references " + conf.tabPrefix + field.type.name + " (");
        boolean first = true;
        for (Field key : keys) {
          out.print(first ? "" :", ");
          first = false;
          out.print(key.name);
        }
        out.println(')' + conf.separator);
      }
    }
  }
  
  private void printKeyTable(Table table, PrintStream out) {
    out.println("create table " + conf.tabPrefix + table.name + " (");
    List<String> keyNames = new ArrayList<>();
    
    for (Field field : table.keys) {
      List<SimpleType> types = new ArrayList<>();
      field.type.assignSimpleTypes(types);
      if (types.size() == 0) throw new StruParseException("No key types for " + field.type);
      if (types.size() == 1) {
        out.println("  " + field.name + ' ' + sqld().sqlType(types.get(0)) + " not null,");
        keyNames.add(field.name);
      } else {
        int index = 1;
        for (SimpleType st : types) {
          String keyName = field.name + index++;
          out.println("  " + keyName + ' ' + sqld().sqlType(st) + " not null,");
          keyNames.add(keyName);
        }
      }
    }
    
    if (conf.cre != null) {
      out.println("  " + conf.cre + ' ' + sqld().timestamp() + " default "
          + sqld().current_timestamp() + " not null,");
    }
    if (conf.userIdFieldType != null && conf.createdBy != null) {
      String type = sqld().userIdFieldType(conf.userIdFieldType, conf.userIdLength);
      
      out.println("  " + conf.createdBy + ' ' + type + ',');
    }
    
    StringBuilder sb = new StringBuilder();
    for (String name : keyNames) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(name);
    }
    out.println("  primary key(" + sb + ")");
    out.println(')' + conf.separator);
  }
  
  private void printFieldsOperTable(Field field, PrintStream out) {
    
    List<String> keyFields = new ArrayList<>();
    
    out.println("create table " + conf.oPref + field.table.name + "_" + field.name + " (");
    
    for (Field key : field.table.keys) {
      List<SimpleType> types = new ArrayList<>();
      key.type.assignSimpleTypes(types);
      
      if (types.size() == 0) throw new StruParseException("No simple type for " + key.type.name);
      
      if (types.size() == 1) {
        String sqlType = sqld().sqlType(types.get(0));
        out.println("  " + key.name + " " + sqlType + " not null,");
        keyFields.add(key.name);
      } else {
        int i = 1;
        for (SimpleType stype : types) {
          String name = key.name + i++;
          keyFields.add(name);
          String sqlType = sqld().sqlType(stype);
          out.println("  " + name + " " + sqlType + " not null,");
        }
      }
    }
    
    {
      List<SimpleType> types = new ArrayList<>();
      field.type.assignSimpleTypes(types);
      
      if (types.size() == 0) throw new StruParseException("No simple type for field "
          + field.table.name + "." + field.name);
      
      if (types.size() == 1) {
        String sqlType = sqld().sqlType(types.get(0));
        out.println("  " + field.name + " " + sqlType + ",");
      } else {
        int i = 1;
        for (SimpleType stype : types) {
          String name = field.name + i++;
          String sqlType = sqld().sqlType(stype);
          out.println("  " + name + " " + sqlType + ",");
        }
      }
    }
    
    if (conf.insertedAt != null) {
      out.println("  " + conf.insertedAt + ' ' + sqld().timestamp() + " default "
          + sqld().current_timestamp() + " not null,");
    }
    if (conf.lastModifiedAt != null) {
      out.println("  " + conf.lastModifiedAt + ' ' + sqld().timestamp() + " default "
          + sqld().current_timestamp() + " not null,");
    }
    if (conf.userIdFieldType != null && conf.insertedBy != null) {
      String type = sqld().userIdFieldType(conf.userIdFieldType, conf.userIdLength);
      
      out.println("  " + conf.insertedBy + ' ' + type + ',');
    }
    if (conf.userIdFieldType != null && conf.lastModifiedBy != null) {
      String type = sqld().userIdFieldType(conf.userIdFieldType, conf.userIdLength);
      
      out.println("  " + conf.lastModifiedBy + ' ' + type + ',');
    }
    
    {
      out.print("  primary key (");
      boolean first = true;
      for (String f : keyFields) {
        if (first) {
          first = false;
        } else {
          out.print(", ");
        }
        out.print(f);
      }
      out.println("),");
    }
    {
      out.println("  " + formForeignKey(keyFields, field.table));
    }
    
    out.println(")" + conf.separator);
  }
  
  private void printFieldsTable(Field field, PrintStream out) {
    
    List<String> keyFields = new ArrayList<>();
    
    out.println("create table " + conf.tabPrefix + field.table.name + "_" + field.name + " (");
    
    for (Field key : field.table.keys) {
      List<SimpleType> types = new ArrayList<>();
      key.type.assignSimpleTypes(types);
      
      if (types.size() == 0) throw new StruParseException("No simple type for " + key.type.name);
      
      if (types.size() == 1) {
        String sqlType = sqld().sqlType(types.get(0));
        out.println("  " + key.name + " " + sqlType + " not null,");
        keyFields.add(key.name);
      } else {
        int i = 1;
        for (SimpleType stype : types) {
          String name = key.name + i++;
          keyFields.add(name);
          String sqlType = sqld().sqlType(stype);
          out.println("  " + name + " " + sqlType + " not null,");
        }
      }
    }
    
    out.println("  " + conf.ts + " " + sqld().timestamp() + " default "
        + sqld().current_timestamp() + " not null,");
    
    if (conf.userIdFieldType != null && conf.modi != null) {
      String type = sqld().userIdFieldType(conf.userIdFieldType, conf.userIdLength);
      
      out.println("  " + conf.modi + ' ' + type + ',');
    }
    
    {
      List<SimpleType> types = new ArrayList<>();
      field.type.assignSimpleTypes(types);
      
      if (types.size() == 0) throw new StruParseException("No simple type for field "
          + field.table.name + "." + field.name);
      
      if (types.size() == 1) {
        String sqlType = sqld().sqlType(types.get(0));
        out.println("  " + field.name + " " + sqlType + ",");
      } else {
        int i = 1;
        for (SimpleType stype : types) {
          String name = field.name + i++;
          String sqlType = sqld().sqlType(stype);
          out.println("  " + name + " " + sqlType + ",");
        }
      }
    }
    
    {
      out.print("  primary key (");
      for (String f : keyFields) {
        out.print(f + ", ");
      }
      out.println(conf.ts + "),");
    }
    {
      out.println("  " + formForeignKey(keyFields, field.table));
    }
    
    out.println(")" + conf.separator);
  }
  
  private String formForeignKey(List<String> fieldNames, Table table) {
    StringBuilder sb = new StringBuilder();
    sb.append("foreign key (");
    for (String key : fieldNames) {
      sb.append(key).append(", ");
    }
    sb.setLength(sb.length() - 2);
    sb.append(") references ").append(conf.tabPrefix).append(table.name).append(" (");
    for (Field field : table.keys) {
      List<SimpleType> types = new ArrayList<>();
      field.type.assignSimpleTypes(types);
      if (types.size() == 1) {
        sb.append(field.name).append(", ");
      } else {
        for (int i = 1, C = types.size(); i <= C; i++) {
          sb.append(field.name + i).append(", ");
        }
      }
    }
    sb.setLength(sb.length() - 2);
    sb.append(')');
    return sb.toString();
  }
  
  private void printViews(List<String> tnames, PrintStream out) {
    for (String tname : tnames) {
      Table table = sg.stru.tables.get(tname);
      for (Field field : table.fields) {
        if (conf.genOperTables) {
          printFieldViewToOper(out, field);
        } else {
          printFieldView(out, field);
          out.println();
        }
      }
      printTableView(out, table);
      out.println();
    }
  }
  
  private void printFieldViewToOper(PrintStream out, Field field) {
    out.println("create     view " + conf.vPrefix + field.table.name + "_" + field.name + " as");
    out.println("  select * from " + conf.oPref + field.table.name + "_" + field.name
        + conf.separator);
  }
  
  private void printFieldView(PrintStream out, Field field) {
    StringBuilder sb = new StringBuilder();
    sb.append("create view " + conf.vPrefix + field.table.name + "_" + field.name + " as\n");
    viewFormer.formFieldSelect(sb, field, null, 2, 0);
    out.println(sb + conf.separator);
  }
  
  private void printTableView(PrintStream out, Table table) {
    StringBuilder sb = new StringBuilder();
    sb.append("create view " + conf.vPrefix + table.name + " as\n");
    viewFormer.formTableSelect(sb, table, null, 2, 0);
    out.println(sb + conf.separator);
  }
  
  private void printInsertFunctions(List<String> tnames, PrintStream out) {
    for (String tname : tnames) {
      Table table = sg.stru.tables.get(tname);
      printTableInsertFunction(out, table);
      for (Field field : table.fields) {
        printFieldInsertFunction(out, field);
      }
    }
  }
  
  /**
   * Генерация хранимой логики для таблицы
   * 
   * <p>
   * у каждой БД, синтаксис хранимых процедур/функций сильно различен, поэтому эта генерация
   * вынесена в дочерние классы
   * </p>
   * 
   * @param out
   *          место куда выводятся SQL-и
   * @param table
   *          таблица, для которой генерируются SQL-и
   */
  protected abstract void printTableInsertFunction(PrintStream out, Table table);
  
  /**
   * Генерация хранимой логики для поля
   * 
   * <p>
   * у каждой БД, синтаксис хранимых процедур/функций сильно различен, поэтому эта генерация
   * вынесена в дочерние классы
   * </p>
   * 
   * @param out
   *          место куда выводятся SQL-и
   * @param field
   *          поле, для которого генерируются SQL-и
   */
  protected abstract void printFieldInsertFunction(PrintStream out, Field field);
  
  /**
   * Генерация java-классов моделей данных и DAO-интерфейсов для MyBatis или GBatis
   */
  public void generateJava() {
    
    checkIdLengths();
    
    Set<String> set = new HashSet<>();
    
    for (Table table : sg.stru.tables.values()) {
      
      List<Field> fields = new ArrayList<>();
      fields.addAll(table.fields);
      fields.addAll(table.keys);
      
      for (Field field : fields) {
        if (field.type instanceof EnumType) {
          EnumType et = (EnumType)field.type;
          if (et.pack == null) et.pack = conf.modelStruPackage;
          if (!set.contains(et.objectType())) {
            set.add(et.objectType());
            generateEnum(et);
          }
        }
      }
    }
    
    for (Table table : sg.stru.tables.values()) {
      ClassOuter fieldsClass = generateFieldsJava(table);
      ClassOuter java = generateJava(table, fieldsClass);
      generateDao(table, java, fieldsClass);
    }
    
    generateIfaces();
  }
  
  private void checkIdLengths() {
    if (conf.maxIdLength == null) return;
    
    for (Table table : sg.stru.tables.values()) {
      for (Field field : table.fields) {
        checkIdLength(conf.vPrefix + table.name + '_' + field.name);
        checkIdLength(conf.tabPrefix + table.name + '_' + field.name);
        checkIdLength(conf._p_ + table.name + '_' + field.name);
      }
    }
    
  }
  
  private void checkIdLength(String id) {
    if (id.length() > conf.maxIdLength) throw new TooLongIdException(id);
  }
  
  private ClassOuter generateFieldsJava(Table table) {
    ClassOuter ou = new ClassOuter(conf.modelStruPackage + table.subpackage(), "Fields", table.name);
    
    if (conf.modelStruExtends != null && conf.modelStruImplements != null) {
      throw new IllegalArgumentException("I do not know what to do: implements "
          + conf.modelStruImplements + " or extends " + conf.modelStruExtends);
    }
    
    String _parent_ = "";
    
    if (conf.modelStruExtends != null) {
      _parent_ = " extends " + ou._(conf.modelStruExtends);
    }
    if (conf.modelStruImplements != null) {
      _parent_ = " implements " + ou._(conf.modelStruImplements);
    }
    
    ou.println("public abstract class " + ou.className + _parent_ + " {");
    
    for (Field field : table.fields) {
      for (FieldDb fi : field.dbFields()) {
        FieldOuter f = ou.addField(fi.javaType, fi.name);
        ou.println("public " + ou._(f.type.objectType()) + " " + f.name + ";");
      }
    }
    
    {
      ou.println("public " + ou.className + " assign(" + ou.className + " from) {");
      ou.println("if (from == null) return this;");
      for (FieldOuter f : ou.fields) {
        ou.println("this." + f.name + " = from." + f.name + ";");
      }
      ou.println("return this;");
      ou.println("}");
    }
    
    for (FieldOuter fo : ou.fields) {
      if (SimpleType.tbool.equals(fo.type)) {
        ou.println("public int get" + firstUpper(fo.name) + "Int() {");
        ou.println("  return (" + fo.name + " != null && " + fo.name + ")?1:0;");
        ou.println("}");
      }
    }
    
    ou.generateTo(conf.javaGenStruDir);
    
    return ou;
  }
  
  private ClassOuter generateJava(Table table, ClassOuter fieldsClass) {
    ClassOuter java = new ClassOuter(conf.modelPackage + table.subpackage(), "", table.name);
    java.println("public class " + java.className + " extends " + java._(fieldsClass.name()) + " {");
    
    for (Field f : table.keys) {
      for (FieldDb fi : f.dbFields()) {
        java.println("public " + java._(fi.javaType.javaType()) + " " + fi.name + ";");
      }
    }
    
    {
      java.println("@Override public int hashCode() {");
      java.print("return " + java._(ARRAYS) + ".hashCode(new Object[] {");
      for (Field f : table.keys) {
        for (FieldDb fi : f.dbFields()) {
          java.print(fi.name + ",");
        }
      }
      java.println(" });");
      java.println("}");
    }
    {
      java.println("@Override public boolean equals(Object obj) {");
      java.println("if (this == obj) return true;");
      java.println("if (obj == null) return false;");
      java.println("if (getClass() != obj.getClass()) return false;");
      java.println(java.className + " other = (" + java.className + ")obj;");
      boolean first = true;
      for (Field field : table.keys) {
        for (FieldDb fi : field.dbFields()) {
          java.print(first ? "return " :"&& ");
          first = false;
          java.print(java._(OBJECTS) + ".equals(" + fi.name + ", other." + fi.name + ")");
        }
      }
      java.println(";");
      java.println("}");
    }
    
    java.println();
    java.println("public " + java.className + "() {}");
    java.println();
    
    {
      java.print("public " + java.className + "(");
      boolean first = true;
      for (Field field : table.keys) {
        for (FieldDb fi : field.dbFields()) {
          java.print(first ? "" :", ");
          first = false;
          java.print(java._(fi.javaType.javaType()) + " " + fi.name);
        }
      }
      java.println(") {");
      
      for (Field field : table.keys) {
        for (FieldDb fi : field.dbFields()) {
          java.println("this." + fi.name + " = " + fi.name + ";");
        }
      }
      
      java.println("}");
    }
    
    for (Command command : table.commands) {
      if (command instanceof ToDictionary) {
        generateJavaToDictionary(java, table, (ToDictionary)command);
      }
    }
    
    java.generateTo(conf.javaGenDir);
    return java;
  }
  
  private void generateEnum(EnumType et) {
    if (et.as != null) return;
    ClassOuter ret = new ClassOuter(et.pack(), "", et.name);
    ret.println("public enum " + ret.className + " {");
    
    ret.print("  ");
    for (String name : et.values) {
      ret.print(name + ", ");
    }
    ret.println(";");
    ret.println("");
    
    ret.println("public static " + ret.className + " valueOfOrNull(String str) {");
    ret.println("  try { return valueOf(str);");
    ret.println("  } catch (java.lang.IllegalArgumentException e) {");
    ret.println("    return null;");
    ret.println("  }");
    ret.println("}");
    
    ret.generateTo(conf.javaGenStruDir);
  }
  
  class DaoClasses {
    ClassOuter commonDao;
    ClassOuter postgres;
    ClassOuter oracle;
    
    public DaoClasses(ClassOuter common, ClassOuter postgres, ClassOuter oracle) {
      this.commonDao = common;
      this.postgres = postgres;
      this.oracle = oracle;
    }
  }
  
  private DaoClasses generateDao(Table table, ClassOuter java, ClassOuter fieldsClass) {
    
    ClassOuter comm = new ClassOuter(conf.daoPackage + table.subpackage(), "", table.name
        + conf.daoSuffix);
    ClassOuter postgres = new ClassOuter(conf.daoPackage + ".postgres" + table.subpackage(), "",
        table.name + "Postgres" + conf.daoSuffix);
    ClassOuter oracle = new ClassOuter(conf.daoPackage + ".oracle" + table.subpackage(), "",
        table.name + "Oracle" + conf.daoSuffix);
    
    comm.println("public interface " + comm.className + " {");
    
    if (libType == LibType.GBATIS) postgres.println("@" + postgres._(GBATIS_AUTOIMPL));
    postgres.println("public interface " + postgres.className //
        + " extends " + postgres._(comm.name()) + "{");
    
    if (libType == LibType.GBATIS) oracle.println("@" + oracle._(GBATIS_AUTOIMPL));
    oracle.println("public interface " + oracle.className //
        + " extends " + oracle._(comm.name()) + "{");
    
    if (table.hasSequence()) {
      SimpleType type = (SimpleType)table.keys.get(0).type;
      comm.println("  " + type.javaType + " next();");
      String seq = conf.seqPrefix + table.name;
      
      postgres.println("  @Override");
      postgres.println("  @" + annSele(postgres) + "(\"select nextval('" + seq + "')\")");
      postgres.println("  " + type.javaType + " next();");
      
      oracle.println("  @Override");
      oracle.println("  @" + annSele(oracle) + "(\"select " + seq + ".nextval from dual\")");
      oracle.println("  " + type.javaType + " next();");
      
    }
    
    comm.println();
    generateDaoTableInserts(comm, table, java, fieldsClass);
    generateDaoTableLoad(comm, table, java, fieldsClass);
    generateDaoTableLoadAt(comm, table, java, fieldsClass);
    for (Field field : table.fields) {
      generateDaoFieldInserts(comm, field, java, fieldsClass);
    }
    
    generateDaoTableCommands(comm, table, java, fieldsClass);
    
    comm.generateTo(conf.javaGenDir);
    postgres.generateTo(conf.javaGenDir);
    oracle.generateTo(conf.javaGenDir);
    
    return new DaoClasses(comm, postgres, oracle);
  }
  
  private void generateDaoTableLoad(ClassOuter comm, Table table, ClassOuter java,
      ClassOuter fieldsClass) {
    
    List<FieldDb> keyInfo = table.dbKeys();
    {
      comm.print("  @" + annSele(comm) + "(\"select * from " + conf.vPrefix + table.name);
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? " where " :" and ");
          first = false;
          comm.print(fi.name + " = #{" + fi.name + "}");
        }
      }
      comm.println("\")");
      
      comm.print("  " + comm._(java.name()) + " load(");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? "" :", ");
          first = false;
          comm.print("@" + annPrm(comm) + "(\"" + fi.name + "\")");
          comm.print(comm._(fi.javaType.javaType()) + " " + fi.name);
        }
      }
      comm.println(");");
    }
    if (table.keys.size() > 1) {
      {
        List<SimpleType> tmp = new ArrayList<>();
        table.keys.get(table.keys.size() - 1).type.assignSimpleTypes(tmp);
        for (int i = 0, C = tmp.size(); i < C; i++) {
          keyInfo.remove(keyInfo.size() - 1);
        }
      }
      
      comm.print("  @" + annSele(comm) + "(\"select * from " + conf.vPrefix + table.name);
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? " where " :" and ");
          first = false;
          comm.print(fi.name + " = #{" + fi.name + "}");
        }
      }
      comm.println("\")");
      
      comm.print("  " + comm._(LIST) + "<" + comm._(java.name()) + "> loadList(");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? "" :", ");
          first = false;
          comm.print("@" + annPrm(comm) + "(\"" + fi.name + "\")");
          comm.print(comm._(fi.javaType.javaType()) + " " + fi.name);
        }
      }
      comm.println(");");
    }
  }
  
  private String annSele(ClassOuter comm) {
    return libType == LibType.MYBATIS ? comm._(MYBATIS_SELECT) :comm._(GBATIS_SELE);
  }
  
  private String annPrm(ClassOuter comm) {
    return libType == LibType.MYBATIS ? comm._(MYBATIS_PARAM) :comm._(GBATIS_PRM);
  }
  
  private void generateDaoTableInserts(ClassOuter comm, Table table, ClassOuter java,
      ClassOuter fieldsClass) {
    
    List<FieldDb> keyInfo = table.dbKeys();
    {
      if (libType == LibType.MYBATIS) {
        comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
            + comm._(MYBATIS_StatementType) + ".CALLABLE)");
      }
      
      comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + table.name);
      comm.print(" (");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? "" :", ");
          first = false;
          comm.print("#{" + fi.name + "}");
        }
      }
      comm.println(")}\")");
      comm.print("  void ins(");
      comm.print(comm._(java.name()) + " " + table.name);
      comm.println(");");
    }
    {
      if (libType == LibType.MYBATIS) {
        comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
            + comm._(MYBATIS_StatementType) + ".CALLABLE)");
      }
      comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + table.name);
      comm.print(" (");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? "" :", ");
          first = false;
          comm.print("#{" + fi.name + "}");
        }
      }
      comm.println(")}\")");
      comm.print("  void add(");
      
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? "" :", ");
          first = false;
          
          comm.print("@" + annPrm(comm) + "(\"" + fi.name + "\")");
          comm.print(comm._(fi.javaType.javaType()) + " " + fi.name);
        }
      }
      comm.println(");");
    }
  }
  
  private String annCall(ClassOuter comm) {
    return libType == LibType.MYBATIS ? comm._(MYBATIS_UPDATE) :comm._(GBATIS_CALL);
  }
  
  private void generateDaoFieldInserts(ClassOuter comm, Field field, ClassOuter java,
      ClassOuter fieldsClass) {
    
    List<FieldDb> keyInfo = field.table.dbKeys();
    List<FieldDb> fieldInfo = field.dbFields();
    List<FieldDb> all = new ArrayList<>();
    all.addAll(keyInfo);
    all.addAll(fieldInfo);
    {
      insField(comm, field, java, all);
      if (SimpleType.ttime.equals(field.type)) {
        insFieldWithNow(comm, field, java, keyInfo, all);
      }
    }
    {
      setField(comm, field, keyInfo, fieldInfo, all);
      if (SimpleType.ttime.equals(field.type)) {
        setFieldWithNow(comm, field, keyInfo, all);
      }
    }
  }
  
  private void setFieldWithNow(ClassOuter comm, Field field, List<FieldDb> keyInfo,
      List<FieldDb> all) {
    
    if (libType == LibType.MYBATIS) {
      comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
          + comm._(MYBATIS_StatementType) + ".CALLABLE)");
    }
    
    comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + field.table.name + "_" + field.name);
    
    comm.print(" (");
    {
      for (FieldDb fi : keyInfo) {
        if (SimpleType.tbool.equals(fi.javaType)) {
          comm.print("#{" + fi.name + "Int}, ");
        } else {
          comm.print("#{" + fi.name + "}, ");
        }
      }
    }
    comm.println("moment())}\")");
    comm.print("  void set" + firstUpper(field.name) + "WithNow(");
    {
      boolean first = true;
      for (FieldDb fi : keyInfo) {
        comm.print(first ? "" :", ");
        first = false;
        comm.print("@" + annPrm(comm) + "(\"" + fi.name + "\")" + comm._(fi.javaType.javaType())
            + " " + fi.name);
      }
    }
    comm.println(");");
  }
  
  private void setField(ClassOuter comm, Field field, List<FieldDb> keyInfo,
      List<FieldDb> fieldInfo, List<FieldDb> all) {
    
    if (libType == LibType.MYBATIS) {
      comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
          + comm._(MYBATIS_StatementType) + ".CALLABLE)");
    }
    
    comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + field.table.name + "_" + field.name);
    
    comm.print(" (");
    {
      boolean first = true;
      for (FieldDb fi : all) {
        comm.print(first ? "" :", ");
        first = false;
        if (SimpleType.tbool.equals(fi.javaType)) {
          comm.print("#{" + fi.name + "Int}");
        } else {
          comm.print("#{" + fi.name + "}");
        }
      }
    }
    comm.println(")}\")");
    comm.print("  void set" + firstUpper(field.name) + "(");
    {
      boolean first = true;
      for (FieldDb fi : keyInfo) {
        comm.print(first ? "" :", ");
        first = false;
        comm.print("@" + annPrm(comm) + "(\"" + fi.name + "\")" + comm._(fi.javaType.javaType())
            + " " + fi.name);
      }
    }
    {
      for (FieldDb fi : fieldInfo) {
        if (SimpleType.tbool.equals(fi.javaType)) {
          comm.print(", @" + annPrm(comm) + "(\"" + fi.name + "Int\") int " + fi.name + "Int");
        } else {
          comm.print(", @" + annPrm(comm) + "(\"" + fi.name + "\")"
              + comm._(fi.javaType.objectType()) + " " + fi.name);
        }
      }
    }
    comm.println(");");
  }
  
  private void insFieldWithNow(ClassOuter comm, Field field, ClassOuter java,
      List<FieldDb> keyInfo, List<FieldDb> all) {
    
    if (libType == LibType.MYBATIS) {
      comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
          + comm._(MYBATIS_StatementType) + ".CALLABLE)");
    }
    
    comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + field.table.name + "_" + field.name);
    comm.print(" (");
    {
      for (FieldDb fi : keyInfo) {
        if (SimpleType.tbool.equals(fi.javaType)) {
          comm.print("#{" + fi.name + "Int}, ");
        } else {
          comm.print("#{" + fi.name + "}, ");
        }
      }
    }
    comm.println("moment())}\")");
    comm.println("  void ins" + firstUpper(field.name) + "WithNow(" + comm._(java.name()) + " "
        + field.table.name + ");");
  }
  
  private void insField(ClassOuter comm, Field field, ClassOuter java, List<FieldDb> all) {
    if (libType == LibType.MYBATIS) {
      comm.println("  @" + comm._(MYBATIS_OPTIONS) + "(statementType = "
          + comm._(MYBATIS_StatementType) + ".CALLABLE)");
    }
    
    comm.print("  @" + annCall(comm) + "(\"{call " + conf._p_ + field.table.name + "_" + field.name);
    
    comm.print(" (");
    {
      boolean first = true;
      for (FieldDb fi : all) {
        comm.print(first ? "" :", ");
        first = false;
        if (SimpleType.tbool.equals(fi.javaType)) {
          comm.print("#{" + fi.name + "Int}");
        } else {
          comm.print("#{" + fi.name + "}");
        }
      }
    }
    comm.println(")}\")");
    comm.println("  void ins" + firstUpper(field.name) + "(" + comm._(java.name()) + " "
        + field.table.name + ");");
  }
  
  private void generateDaoTableLoadAt(ClassOuter comm, Table table, ClassOuter java,
      ClassOuter fieldsClass) {
    StringBuilder sb = new StringBuilder();
    viewFormer.formTableSelect(sb, table, "tts.ts", 0, 0);
    
    List<FieldDb> keyInfo = table.dbKeys();
    {
      comm.print("  @" + annSele(comm) + "(\"with tts as (select #{ts} ts from dual), xx as (" + sb
          + ") select * from xx");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? " where " :" and ");
          first = false;
          comm.print("xx." + fi.name + " = #{" + fi.name + "}");
        }
      }
      comm.println("\")");
      
      comm.print("  " + comm._(java.name()) + " loadAt(@" + annPrm(comm) + "(\"" + conf.ts + "\")"
          + comm._(DATE) + " " + conf.ts);
      for (FieldDb fi : keyInfo) {
        comm.print(", @" + annPrm(comm) + "(\"" + fi.name + "\")");
        comm.print(comm._(fi.javaType.javaType()) + " " + fi.name);
      }
      comm.println(");");
    }
    if (table.keys.size() > 1) {
      {
        List<SimpleType> tmp = new ArrayList<>();
        table.keys.get(table.keys.size() - 1).type.assignSimpleTypes(tmp);
        for (int i = 0, C = tmp.size(); i < C; i++) {
          keyInfo.remove(keyInfo.size() - 1);
        }
      }
      
      comm.print("  @" + annSele(comm) + "(\"with tts as (select #{ts} ts from dual), xx as (" + sb
          + ") select * from xx");
      {
        boolean first = true;
        for (FieldDb fi : keyInfo) {
          comm.print(first ? " where " :" and ");
          first = false;
          comm.print("xx." + fi.name + " = #{" + fi.name + "}");
        }
      }
      comm.println("\")");
      
      comm.print("  " + comm._(LIST) + "<" + comm._(java.name()) + "> loadListAt(@" + annPrm(comm)
          + "(\"" + conf.ts + "\")" + comm._(DATE) + " " + conf.ts);
      for (FieldDb fi : keyInfo) {
        comm.print(", @" + annPrm(comm) + "(\"" + fi.name + "\")");
        comm.print(comm._(fi.javaType.javaType()) + " " + fi.name);
      }
      comm.println(");");
    }
  }
  
  private void generateIfaces() {
    generateTandV();
    generateVT();
  }
  
  private void generateTandV() {
    ClassOuter t = new ClassOuter(conf.daoPackage + ".i", "", "T");
    ClassOuter v = new ClassOuter(conf.daoPackage + ".i", "", "V");
    t.println("public interface " + t.className + " {");
    v.println("public interface " + v.className + " {");
    
    for (Table table : sortTablesByName(sg.stru.tables.values())) {
      t.println("  String " + table.name + " = \"" + conf.tabPrefix + table.name + "\";");
      v.println("  String " + table.name + " = \"" + conf.vPrefix + table.name + "\";");
      for (Field field : sortFieldsByName(table.fields)) {
        t.println("  String " + table.name + "_" + field.name + " = \"" + conf.tabPrefix
            + table.name + "_" + field.name + "\";");
        v.println("  String " + table.name + "_" + field.name + " = \"" + conf.vPrefix + table.name
            + "_" + field.name + "\";");
      }
      t.println();
      v.println();
    }
    
    t.generateTo(conf.javaGenDir);
    v.generateTo(conf.javaGenDir);
  }
  
  private List<Field> sortFieldsByName(List<Field> fields) {
    List<Field> ret = new ArrayList<>();
    ret.addAll(fields);
    
    Collections.sort(ret, new Comparator<Field>() {
      @Override
      public int compare(Field o1, Field o2) {
        return o1.name.compareTo(o2.name);
      }
    });
    
    return ret;
  }
  
  private static List<Table> sortTablesByName(Collection<Table> tables) {
    List<Table> ret = new ArrayList<>();
    ret.addAll(tables);
    
    Collections.sort(ret, new Comparator<Table>() {
      @Override
      public int compare(Table o1, Table o2) {
        return o1.name.compareTo(o2.name);
      }
    });
    
    return ret;
  }
  
  private void generateVT() {
    ClassOuter vt = new ClassOuter(conf.daoPackage + ".i", "", "VT");
    vt.println("public interface " + vt.className + " {");
    
    for (Table table : sg.stru.tables.values()) {
      StringBuilder tab = new StringBuilder();
      viewFormer.formTableSelect(tab, table, "tts.ts", 0, 0);
      vt.println("  String " + table.name + " = \"" + tab + "\";");
      for (Field field : table.fields) {
        StringBuilder fi = new StringBuilder();
        viewFormer.formFieldSelect(fi, field, "tts.ts", 0, 0);
        vt.println("  String " + table.name + "_" + field.name + " = \"" + fi + "\";");
      }
      vt.println();
    }
    
    vt.generateTo(conf.javaGenDir);
  }
  
  private void generateDaoTableCommands(ClassOuter comm, Table table, ClassOuter java,
      ClassOuter fieldsClass) {
    for (Command command : table.commands) {
      if (command instanceof SelectAll) {
        generateDaoTableSelectAll(comm, table, java, (SelectAll)command);
        continue;
      }
      if (command instanceof ToDictionary) return;//Ignore here
      throw new StruParseException("Unknown command class " + command.getClass());
    }
  }
  
  private void generateDaoTableSelectAll(ClassOuter comm, Table table, ClassOuter java,
      SelectAll command) {
    String seleAnn = annSele(comm);
    
    comm.println("@" + seleAnn + "(\"select * from " + conf.vPrefix + table.name + " "
        + command.orderBy() + "\")");
    comm.println(comm._(LIST) + "<" + java.className + "> " + command.methodName + "();");
  }
  
  private void generateJavaToDictionary(ClassOuter java, Table table, ToDictionary todict) {
    java.println("public " + java._(todict.toClass) + " toDictionary() {");
    
    {
      java.print("  return new " + java._(todict.toClass) + "(");
      boolean first = true;
      for (FieldDb fi : table.dbKeys()) {
        java.print(first ? "" :", ");
        first = false;
        java.print(fi.name);
      }
      if (todict.more != null && todict.more.trim().length() > 0) {
        java.print(", " + todict.more);
      }
      java.println(");");
    }
    
    java.println("}");
  }
  
  private static boolean def(String comment) {
    if (comment == null) return false;
    return comment.trim().length() > 0;
  }
  
  /**
   * Распечатка SQL-ей для генерации коментов к таблицам и полям
   * 
   * @param out
   *          место вывода SQL-ей
   **/
  public void printComment(PrintStream out) {
    for (Table table : sg.stru.tables.values()) {
      printTableComments(out, table);
      printFieldComments(out, table);
    }
  }
  
  private void printTableComments(PrintStream out, Table table) {
    String tableName = conf.tabPrefix + table.name;
    {
      String comment = sg.stru.tableComment.get(table.name);
      if (def(comment)) {
        out.println("COMMENT ON TABLE " + tableName + " IS '" + comment + "'" + conf.separator);
      }
    }
    for (FieldDb fi : table.dbKeys()) {
      out.println("comment on column " + tableName + '.' + fi.name
          + " is 'Ключевое поле материнской таблицы'" + conf.separator);
    }
    out.println("comment on column " + tableName + '.' + conf.cre + " is 'Момент создания записи'"
        + conf.separator);
  }
  
  private void printFieldComments(PrintStream out, Table table) {
    String tableName = conf.tabPrefix + table.name;
    for (Field field : table.fields) {
      String fieldTableName = tableName + "_" + field.name;
      {
        String comment = sg.stru.fieldComment.get(table.name + '.' + field.name);
        if (def(comment)) {
          out.println("COMMENT ON TABLE " + fieldTableName + " IS '" + comment + "'"
              + conf.separator);
        }
      }
      for (FieldDb fi : table.dbKeys()) {
        out.println("comment on column " + fieldTableName + '.' + fi.name
            + " is 'Ссылка на ключевое поле материнской таблицы'" + conf.separator);
      }
      
      for (FieldDb fi : field.dbFields()) {
        out.println("comment on column " + fieldTableName + '.' + fi.name + " is 'Значение поля'"
            + conf.separator);
      }
      
      out.println("comment on column " + fieldTableName + '.' + conf.ts
          + " is 'Момент последнего изменения значения'" + conf.separator);
    }
  }
  
  protected void printUpdateSql(PrintStream out, String tname, Field field) {
    out.print("    update " + tname);
    {
      {
        boolean first = true;
        for (FieldDb fi : field.dbFields()) {
          out.print(first ? " set " :", ");
          first = false;
          out.print(fi.name + " = " + fi.name + "__");
        }
        if (conf.lastModifiedAt != null) {
          out.print(", " + conf.lastModifiedAt + " = " + sqld().current_timestamp());
        }
        if (conf.userIdFieldType != null && conf.lastModifiedBy != null) {
          out.print(", " + conf.lastModifiedBy + " = " + conf.get_changer + "()");
        }
      }
      {
        boolean first = true;
        for (Field key : field.table.keys) {
          for (FieldDb fi : key.dbFields()) {
            out.print(first ? " where " :" and ");
            first = false;
            out.print(fi.name + " = " + fi.name + "__");
          }
        }
      }
    }
    out.println(" ; ");
  }
  
  protected void printInsertSql(PrintStream out, //
      String tname, Field field, String... changerFields) {
    printInsertCommand("      ", out, tname, field, changerFields);
  }
  
  protected void printInsertCommand(String s, PrintStream out, //
      String tname, Field field, String... changerFields) {
    out.print(s + "insert into " + tname + " (");
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "" :", ");
          first = false;
          out.print(fi.name);
        }
      }
      for (FieldDb fi : field.dbFields()) {
        out.print(", " + fi.name);
      }
    }
    
    if (conf.userIdFieldType != null && changerFields.length > 0) {
      for (String fld : changerFields) {
        if (fld != null) out.print(", " + fld);
      }
    }
    
    out.print(") values (");
    
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "" :", ");
          first = false;
          out.print(fi.name + "__");
        }
      }
      for (FieldDb fi : field.dbFields()) {
        out.print(", " + fi.name + "__");
      }
    }
    
    if (conf.userIdFieldType != null && changerFields.length > 0) {
      for (String fld : changerFields) {
        if (fld != null) out.print(", " + conf.get_changer + "()");
      }
    }
    
    out.println(") ; ");
  }
}
