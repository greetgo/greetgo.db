package kz.greetgo.sqlmanager.parser;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import kz.greetgo.sqlmanager.model.*;
import kz.greetgo.sqlmanager.model.command.Command;
import kz.greetgo.sqlmanager.model.command.SelectAll;
import kz.greetgo.sqlmanager.model.command.ToDictionary;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static kz.greetgo.sqlmanager.model.SimpleType.SimpleTypes;

/**
 * Формирователь структуры классов в форме NF3
 *
 * @author pompei
 */
@SuppressFBWarnings("DMI_COLLECTION_OF_URLS")
public class StruShaper {
  /**
   * Постфикс файлов, в которых содержатся коменты к таблицам
   */
  private static final String COMMENT = ".comment";

  /**
   * DOM для NF3
   */
  public final Stru stru = new Stru();

  /**
   * Папка, в которой лежат файлы коментов
   */
  private URL commentDir = null;

  private static final Pattern ORDER_BY = Pattern.compile(".*orderBy\\s*\\(([^\\)]+)\\).*");
  private static final Pattern TO_DICT = Pattern.compile("\\s*(\\S+)\\s+(.*)");

  /**
   * Помощник для парсинга с дополнительных команд
   *
   * @author pompei
   */
  private class PCommand {
    final PTable owner;
    final String name, description;

    public PCommand(PTable owner, String name, String description) {
      this.owner = owner;
      this.name = name;
      this.description = description;
    }

    public Command parse() {
      if ("SelectAll".equals(name)) {
        String orderBy = "";
        {
          Matcher m = ORDER_BY.matcher(description);
          if (m.matches()) {
            orderBy = m.group(1);
          }
        }

        String methodName = description;
        int idx = description.indexOf(' ');
        if (idx >= 0) {
          methodName = description.substring(0, idx);
        }

        return new SelectAll(methodName.trim(), orderBy.trim());
      }

      if ("ToDictionary".equals(name)) {
        Matcher m = TO_DICT.matcher(description);
        if (!m.matches()) throw new StruParseException("Cannot parse ToDictionary in table "
            + owner.name);
        return new ToDictionary(m.group(1), m.group(2).trim());
      }

      throw new StruParseException("Cannot parse command [" + name + "] [" + description + "]: table "
          + owner.name);
    }
  }

  /**
   * Помощник для парсинга таблиц
   *
   * @author pompei
   */
  private class PTable {
    final String subpackage;
    final String name;
    String description;
    final List<PField> keyFields = new ArrayList<>();
    final List<PField> fields = new ArrayList<>();
    final List<PCommand> commands = new ArrayList<>();

    Type type;
    public String parsedTopic;

    public boolean isSimpleType() {
      if (type == null) return false;
      return type instanceof SimpleType;
    }

    public PTable(String name, String description, String subpackage) {
      this.name = name;
      this.description = description;
      this.subpackage = subpackage;
    }

    public void newField(String name, String description) {
      if (name.startsWith("+")) {
        commands.add(new PCommand(this, name.substring(1).trim(), description));
        return;
      }

      boolean isKey = false;
      if (name.startsWith("*")) {
        isKey = true;
        name = name.substring(1);
      }
      for (PField f : fields) {
        if (f.name.equals(name)) {
          throw new StruParseException("Table " + this.name + " already has field " + name);
        }
      }
      for (PField f : keyFields) {
        if (f.name.equals(name)) {
          throw new StruParseException("Table " + this.name + " already has field " + name);
        }
      }

      PField ret = new PField(name, description);
      (isKey ? keyFields : fields).add(ret);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("\n").append(name).append(' ').append(description);
      for (PField f : fields) {
        sb.append("\n  ").append(f.name).append(' ').append(f.description);
      }
      return sb.toString();
    }

    public String shortStr() {
      return "pTable " + name + " ### " + type;
    }

    public void moveAllFields() {
      if (!(type instanceof Table)) return;
      Table table = (Table) type;
      moveFields(keyFields, table.keys);
      moveFields(fields, table.fields);
    }

    private void moveFields(List<PField> pFields, List<Field> fields) {
      for (PField pfield : pFields) {
        fields.add(new Field((Table) type, pfield.name, pfield.table.type));
      }
    }
  }

  /**
   * Помощник для парсинга полей
   *
   * @author pompei
   */
  private class PField {
    final String name, description;

    PTable table = null;

    public PField(String name, String description) {
      this.name = name;
      this.description = description;
    }
  }

  /**
   * Помощник для парсинга енумов
   *
   * @author pompei
   */
  class PEnum extends PTable {
    final List<String> lines = new ArrayList<>();
    final String as;

    public PEnum(String name, String as, String subpackage) {
      super(name, null, subpackage);
      this.as = as;
    }

    public void append(String line) {
      if (as != null) throw new StruParseException(
          "Cannot append values when 'as' defined: enum name = " + name);
      lines.add(line);
    }

    @Override
    public String shortStr() {
      return "pEnum " + name + " ## " + type;
    }
  }

  private final Map<String, PTable> tables = new HashMap<>();

  private PTable curTable = null;
  private PEnum curEnum = null;
  private String currentSubpackage = null;

  public boolean printPStru = false;

  class Pair {
    final String left, right;

    public Pair(String line) {
      if (line == null) {
        left = right = null;
        return;
      }
      String trim = line.trim();
      if (trim.length() == 0 || trim.startsWith("#")) {
        left = right = null;
        return;
      }
      int idx = trim.indexOf(' ');
      if (idx < 0) {
        left = trim;
        right = null;
        return;
      }
      {
        left = trim.substring(0, idx);
        right = trimOrNull(trim.substring(idx));
      }
    }

  }

  private static final Pattern ENUM_NAME = Pattern.compile("\\s*(\\S+)\\s*");
  private static final Pattern ENUM_AS = Pattern.compile("\\s*(\\S+)\\s+as\\s+(\\S+)\\s*",
      Pattern.CASE_INSENSITIVE);

  /**
   * Парсит очереную строку из файла .nf3
   *
   * @param line       очередная строка
   * @param currentUrl УРЛ парсируемого файла
   * @throws Exception проброс для избегания try/catch-ей
   */
  private void appendLine(String line, URL currentUrl) throws Exception {
    if (line == null) return;
    Pair p = new Pair(line);
    if (p.left == null) return;

    if ("import".equals(p.left)) {
      if (p.right == null) throw new NullPointerException("p.right == null");
      parseURL(new URL(currentUrl, p.right));
      return;
    }

    if ("commentDir".equals(p.left)) {
      if (commentDir != null) {
        throw new StruParseException("Повторная инициализация поля commentDir=[" + commentDir
            + "] из " + currentUrl);
      }
      if (p.right == null) throw new NullPointerException("p.right == null");
      commentDir = new URL(currentUrl, p.right);
      return;
    }

    if ("subpackage".equals(p.left)) {
      currentSubpackage = p.right;
      return;
    }

    if (line.startsWith(" ")) {
      if (curTable != null) curTable.newField(p.left, p.right);
      if (curEnum != null) curEnum.append(line);
      return;
    }
    if ("enum".equals(p.left)) {
      curTable = null;
      String enumName = null;
      String as = null;
      if (p.right != null) {
        Matcher m = ENUM_NAME.matcher(p.right);
        if (m.matches()) enumName = m.group(1);
      }
      if (enumName == null && p.right != null) {
        Matcher m = ENUM_AS.matcher(p.right);
        if (m.matches()) {
          enumName = m.group(1);
          as = m.group(2);
        }
      }
      if (enumName == null) throw new StruParseException("Cannot parse enum right part: " + p.right);
      if (tables.keySet().contains(enumName)) {
        throw new StruParseException("Table " + enumName + " already defined");
      }
      curEnum = new PEnum(enumName, as, currentSubpackage);
      tables.put(curEnum.name, curEnum);
      return;
    }

    {
      String tableName = p.left;
      if (tables.keySet().contains(tableName)) {
        throw new StruParseException("Table " + tableName + " already defined");
      }
      curEnum = null;
      curTable = new PTable(tableName, p.right, currentSubpackage);
      tables.put(curTable.name, curTable);
    }
  }

  private static String trimOrNull(String str) {
    if (str == null) return null;
    str = str.trim();
    if (str.length() == 0) return null;
    return str;
  }

  private static final Pattern FROM = Pattern.compile(".*from\\s+([+-]?\\d+).*",
      Pattern.CASE_INSENSITIVE);

  private final Set<URL> parsedUrls = new HashSet<>();

  /**
   * Парсинг очередного файла
   *
   * @param url УРЛ очередного анализируемого файла
   * @throws Exception проброс для избегания try/catch-ей
   */
  public void parseURL(URL url) throws Exception {
    if (parsedUrls.contains(url)) return;
    parsedUrls.add(url);

    InputStream in = url.openStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    while (true) {
      String line = br.readLine();
      if (line == null) break;
      appendLine(line, url);
    }
    br.close();
  }

  private static final Pattern TOPIC = Pattern.compile("\\btopic\\s+(\\S+)\\s*");

  /**
   * Парсинг и пост-обработка
   *
   * @param url УРЛ обрабатываемого файла
   * @throws Exception проброс для избегания try/catch-ей
   */
  public void parse(URL url) throws Exception {

    parseURL(url);

    for (SimpleType sType : SimpleTypes.values()) {
      PTable t = new PTable(sType.name, null, currentSubpackage);
      tables.put(t.name, t);
      t.type = sType;
    }

    for (PTable pTable : tables.values()) {
      if (pTable.isSimpleType()) continue;
      if (pTable instanceof PEnum) continue;

      if (pTable.description != null) {
        Matcher matcher = TOPIC.matcher(pTable.description);
        if (matcher.find()) {
          pTable.parsedTopic = matcher.group(1);
          String s = pTable.description.substring(0, matcher.start(0)) + pTable.description.substring(matcher.end(0));
          s = s.trim();
          if (s.length() == 0) s = null;
          pTable.description = s;
        }
      }

      if (pTable.description != null) {
        if (pTable.keyFields.size() > 0) {
          throw new StruParseException("Table " + pTable.name + " has double definition of keys");
        }
        pTable.newField("*" + pTable.name, pTable.description);
      }

      if (pTable.keyFields.size() == 0) {
        throw new StruParseException("Table " + pTable.name + " has no keys");
      }

      List<PField> all = new ArrayList<>();
      all.addAll(pTable.keyFields);
      all.addAll(pTable.fields);

      PF:
      for (PField f : all) {
        if (f.table != null) continue PF;
        String description = f.description;
        if (f.description == null) description = f.name;

        String[] split = description.split("\\s+");

        if ("enum".equals(split[0])) {
          if (split.length == 1) throw new StruParseException("Enum without values in field "
              + pTable.name + "." + f.name);
          String values[] = new String[split.length - 1];
          for (int i = 1, C = split.length; i < C; i++) {
            values[i - 1] = split[i];
          }

          Type type = new EnumType(pTable.name + "_" + f.name, currentSubpackage, null, values);
          f.table = new PEnum(type.name, null, currentSubpackage);
          f.table.type = type;
          continue PF;
        }

        PTable pTableType = tables.get(split[0]);
        if (pTableType == null) throw new StruParseException("No type " + split[0]);
        f.table = pTableType;
      }

      if (pTable.type == null) {
        Table t = new Table(pTable.name, pTable.subpackage);
        t.parsedTopic = pTable.parsedTopic;
        pTable.type = t;
        stru.tables.put(t.name, t);

        if (pTable.description != null) {
          Matcher m = FROM.matcher(pTable.description);
          if (m.matches()) {
            t.sequenceFrom = Long.parseLong(m.group(1));
          }
        }
      }
    }

    F:
    for (PTable ptable : tables.values()) {
      if (ptable.isSimpleType()) continue;
      if (ptable instanceof PEnum) {
        PEnum penum = (PEnum) ptable;
        List<String> values = new ArrayList<>();
        for (String line : penum.lines) {
          for (String str : line.trim().split("\\s+")) {
            if (values.contains(str)) throw new StruParseException("Double enum value " + str
                + " in for enum " + penum.name);
            values.add(str);
          }
        }
        penum.type = new EnumType(penum.name, currentSubpackage, penum.as,
            values.toArray(new String[values.size()]));
        continue F;
      }
    }

    for (PTable ptable : tables.values()) {
      ptable.moveAllFields();
    }

    for (PTable ptable : tables.values()) {
      for (PCommand com : ptable.commands) {
        ptable.type.commands.add(com.parse());
      }
    }

    readComments();

    if (!printPStru) return;

    for (PTable ptable : tables.values()) {
      //if (pTable.isSimpleType()) continue;

      Set<String> keyNames = new HashSet<>();
      for (PField f : ptable.keyFields) {
        keyNames.add(f.name);
      }

      //System.out.println(t.name + " ### " + t.type);
      System.out.println(ptable.shortStr());
      List<PField> all = new ArrayList<>();
      all.addAll(ptable.keyFields);
      all.addAll(ptable.fields);
      for (PField f : all) {
        String pre = keyNames.contains(f.name) ? "*" : " ";
        System.out.println("  " + pre + f.name + "(" + f.description + ") - "
            + (f.table == null ? "null" : f.table.name));
      }
      System.out.println();
    }

  }

  private void readComments() throws Exception {
    if (commentDir == null) return;
    stru.fieldComment = new HashMap<>();
    stru.tableComment = new HashMap<>();

    for (String tableName : stru.tables.keySet()) {
      parseCommentUrl(new URL(commentDir, tableName + COMMENT), tableName);
    }

    checkComments();
  }

  private String readUrlAsUTF8(URL url) throws IOException {
    StringBuilder sb = new StringBuilder();
    {
      final InputStream in;
      try {
        in = url.openStream();
      } catch (Exception e) {
        throw new CannotOpenUrlStream(url, e);
      }
      BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

      while (true) {
        String line = br.readLine();
        if (line == null) break;
        if (sb.length() > 0) sb.append("\n");
        sb.append(line);
      }
      br.close();
    }
    return sb.toString();
  }

  private static final Pattern SPACE = Pattern.compile("\\s");

  private void parseCommentUrl(URL url, String tableName) throws Exception {

    final String content;
    try {
      content = readUrlAsUTF8(url);
    } catch (CannotOpenUrlStream e) {
      throw new NoComment(new File(e.url.getFile()).getName(), e);
    }

    Table table = stru.tables.get(tableName);
    if (table == null) throw new StruParseException("No table " + tableName
        + " (it is comment parsing)");

    String[] split = content.split("\\$");
    stru.tableComment.put(table.name, split[0].trim());

    for (int i = 1, C = split.length; i < C; i++) {
      String str = split[i].trim();

      Matcher m = SPACE.matcher(str);

      String fieldName = str;
      String comment = null;
      if (m.find()) {
        fieldName = str.substring(0, m.start());
        comment = str.substring(m.start()).trim();
      }

      stru.fieldComment.put(table.name + '.' + fieldName, comment);
    }
  }

  private static boolean no(String comment) {
    if (comment == null) return true;
    return comment.trim().length() == 0;
  }

  private void checkComments() {
    if (stru.tableComment == null) return;

    for (Table table : stru.tables.values()) {
      String tableComment = stru.tableComment.get(table.name);
      if (no(tableComment)) {
        throw new StruParseException("No comment for table " + table.name);
      }
      for (Field field : table.fields) {
        String fieldComment = stru.fieldComment.get(table.name + '.' + field.name);
        if (no(fieldComment)) {
          throw new StruParseException("No comment for field " + table.name + '.' + field.name);
        }
      }
    }
  }

}
