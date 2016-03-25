package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.*;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbStructReader {

  private DbStructReader() {
  }

  private final DbStruct dbStruct = new DbStruct();

  public static DbStruct read(ResourceRef ref) {

    DbStructReader reader = new DbStructReader();

    try {
      reader.readLines(ref);
    } catch (Exception e) {
      if (e instanceof RuntimeException) throw (RuntimeException) e;
      throw new RuntimeException(e);
    }

    reader.readFromLines();

    return reader.dbStruct;
  }

  interface Line {
    void parse();
  }

  private final List<Line> lines = new ArrayList<>();

  private static final Pattern INCLUDE = Pattern.compile("\\s*@\\s*include\\s+(\\S+)\\s*(#.*)?");

  private final Set<ResourceRef> resourceRefCache = new HashSet<>();

  private void readLines(ResourceRef ref) throws Exception {
    if (resourceRefCache.contains(ref)) return;
    resourceRefCache.add(ref);

    final List<String> lines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(ref.getInputStream(), "UTF-8"))) {
      while (true) {
        String line = br.readLine();
        if (line == null) break;
        lines.add(line);
      }
    }

    int lineNumber = 0;
    for (String line : lines) {
      lineNumber++;

      {
        Matcher matcher = INCLUDE.matcher(line);
        if (matcher.matches()) {
          readLines(ref.change(matcher.group(1)));
          this.lines.add(new AutomaticallyFinishInclude());
          continue;
        }
      }

      this.lines.add(new ResourceRefLine(line, ref, lineNumber));
    }
  }

  private void readFromLines() {
    //noinspection Convert2streamapi
    for (Line line : lines) {
      line.parse();
    }
  }

  private String currentSubpackage = null;
  private ParsedType currentType = null;

  enum Current {
    NONE, OPTIONS, ESSENCE
  }

  Current current = Current.NONE;

  private static Class<? extends Enum> loadClass(String enumClassName, Place place) {
    try {
      //noinspection unchecked
      return (Class<? extends Enum>) Class.forName(enumClassName);
    } catch (ClassNotFoundException e) {
      throw new EnumClassNotFound(enumClassName, place, e);
    }
  }

  class EnumDot {
    final String name;
    final Class<? extends Enum> enumClass;
    final Place place;

    public EnumDot(String name, String enumClassName, Place place) {
      this.name = name;
      this.enumClass = loadClass(enumClassName, place);
      this.place = place;
    }
  }

  private final Map<String, EnumDot> enumMap = new HashMap<>();

  class AliasDot {
    final String name, target;
    final Place place;

    public AliasDot(String name, String target, Place place) {
      this.name = name;
      this.target = target;
      this.place = place;
    }
  }

  private final Map<String, AliasDot> aliasMap = new HashMap<>();

  class AutomaticallyFinishInclude implements Line {
    @Override
    public void parse() {
      currentType = null;
      currentSubpackage = null;
      current = Current.NONE;
    }
  }

  private static final Pattern ESSENCE = Pattern.compile("(\\w+)(\\s+\\w+)?\\s*(--(.*))?");
  private static final Pattern FIELD = Pattern.compile("\\s+(\\*\\s*)?(\\w+)(\\s+\\w+)?\\s*(--(.*))?");

  private static final Pattern ENUM = Pattern.compile("\\s*@\\s*enum\\s+(\\S+)\\s+(\\S+)\\s*(#.*)?");
  private static final Pattern SUBPACKAGE = Pattern.compile("\\s*@\\s*subpackage\\s+(\\S+)\\s*(#.*)?");
  private static final Pattern ALIAS = Pattern.compile("\\s*@\\s*alias\\s+(\\w+)\\s+(\\w+)\\s*(#.*)?");

  private static final Pattern OPTIONS = Pattern.compile("\\s*@\\s*options\\s*(#.*)?");
  private static final Pattern OPTION = Pattern.compile("\\s+(\\w+)(\\s+\\S.*)?");

  class ResourceRefLine implements Line {
    private final String line;
    private final Place place;

    public ResourceRefLine(String line, ResourceRef ref, int lineNumber) {
      this.line = line;
      place = new Place(ref, lineNumber);
    }

    @Override
    public void parse() {
      {
        String trimmedLine = line.trim();
        if (trimmedLine.length() == 0) return;
        if (trimmedLine.startsWith("#")) return;
      }

      {
        Matcher matcher = ESSENCE.matcher(line);
        if (matcher.matches()) {
          currentType = new ParsedType(currentSubpackage, matcher.group(1), matcher.group(2), matcher.group(4), place);
          current = Current.ESSENCE;

          {
            ParsedType alreadyExistsType = dbStruct.typeMap.get(currentType.name);
            if (alreadyExistsType != null) {
              throw new DuplicateType(currentType, alreadyExistsType);
            }
          }

          dbStruct.typeMap.put(currentType.name, currentType);
          return;
        }
      }

      {
        Matcher matcher = OPTIONS.matcher(line);
        if (matcher.matches()) {
          current = Current.OPTIONS;
          return;
        }
      }

      if (current == Current.ESSENCE) {
        Matcher matcher = FIELD.matcher(line);
        if (matcher.matches()) {

          if (currentType == null) {
            throw new NoCurrentTypeToAddField(matcher.group(2), place);
          }

          ParsedField field = new ParsedField(matcher.group(1), matcher.group(2),
            matcher.group(3), matcher.group(5), place);

          for (ParsedField f : currentType.fieldList) {
            if (f.name.equals(field.name)) {
              throw new FieldAlreadyExists(field, f);
            }
          }

          currentType.fieldList.add(field);
          return;
        }
      }

      if (current == Current.OPTIONS) {
        Matcher matcher = OPTION.matcher(line);
        if (matcher.matches()) {
          dbStruct.options.parseLine(matcher.group(1), matcher.group(2), place);
          return;
        }
      }

      {
        Matcher matcher = ENUM.matcher(line);
        if (matcher.matches()) {

          EnumDot enumDot = new EnumDot(matcher.group(1), matcher.group(2), place);

          EnumDot alreadyExistsEnumDot = enumMap.get(enumDot.name);
          if (alreadyExistsEnumDot != null) {
            if (alreadyExistsEnumDot.enumClass.equals(enumDot.enumClass)) return;
            throw new DuplicateEnumAlias(enumDot.name, enumDot.place, alreadyExistsEnumDot.place);
          }

          enumMap.put(enumDot.name, enumDot);

          return;
        }
      }

      {
        Matcher matcher = SUBPACKAGE.matcher(line);
        if (matcher.matches()) {
          currentSubpackage = matcher.group(1);
          return;
        }
      }

      {
        Matcher matcher = ALIAS.matcher(line);
        if (matcher.matches()) {

          AliasDot aliasDot = new AliasDot(matcher.group(1), matcher.group(2), place);

          AliasDot alreadyExistsAlias = aliasMap.get(aliasDot.name);
          if (alreadyExistsAlias != null) {
            if (alreadyExistsAlias.target.equals(aliasDot.target)) return;
            throw new AliasAlreadyDefined(aliasDot.name, aliasDot.place, alreadyExistsAlias.place);
          }

          aliasMap.put(aliasDot.name, aliasDot);

          return;
        }
      }

      {
        throw new UnknownLine(line, place);
      }

    }
  }

}
