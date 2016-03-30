package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.EssenceAlreadyDefined;
import kz.greetgo.gbatis2.struct.exceptions.FieldAlreadyExists;
import kz.greetgo.gbatis2.struct.exceptions.UnknownLine;
import kz.greetgo.gbatis2.struct.resource.ResourceRef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbStructReader {

  private DbStructReader() {
  }

  private final DbStruct dbStruct = new DbStruct();

  public static DbStruct read(ResourceRef ref) throws Exception {

    DbStructReader reader = new DbStructReader();

    reader.readLines(ref);
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
  private ParsedEssence currentEssence = null;

  enum State {
    NONE, OPTIONS, ESSENCE
  }

  State state = State.NONE;

  class AutomaticallyFinishInclude implements Line {
    @Override
    public void parse() {
      currentEssence = null;
      currentSubpackage = null;
      state = State.NONE;
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
      place = new PlaceInResourceRef(ref, lineNumber);
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
          currentEssence = new ParsedEssence(currentSubpackage, matcher.group(1), matcher.group(2), matcher.group(4), place);
          state = State.ESSENCE;

          {
            ParsedEssence alreadyExistsType = dbStruct.essenceMap.get(currentEssence.name);
            if (alreadyExistsType != null) {
              throw new EssenceAlreadyDefined(currentEssence, alreadyExistsType);
            }
          }

          dbStruct.essenceMap.put(currentEssence.name, currentEssence);
          return;
        }
      }

      {
        Matcher matcher = OPTIONS.matcher(line);
        if (matcher.matches()) {
          state = State.OPTIONS;
          return;
        }
      }

      if (state == State.ESSENCE) {
        Matcher matcher = FIELD.matcher(line);
        if (matcher.matches()) {

          ParsedField field = new ParsedField(matcher.group(1), matcher.group(2),
              matcher.group(3), matcher.group(5), place);

          for (ParsedField f : currentEssence.fieldList) {
            if (f.name.equals(field.name)) {
              throw new FieldAlreadyExists(field, f);
            }
          }

          currentEssence.fieldList.add(field);
          return;
        }
      }

      if (state == State.OPTIONS) {
        Matcher matcher = OPTION.matcher(line);
        if (matcher.matches()) {
          dbStruct.options.parseLine(matcher.group(1), matcher.group(2), place);
          return;
        }
      }

      {
        Matcher matcher = ENUM.matcher(line);
        if (matcher.matches()) {
          dbStruct.enums.append(new EnumDot(matcher.group(1), matcher.group(2), place));
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
          dbStruct.aliases.append(new AliasDot(matcher.group(1), matcher.group(2), place));
          return;
        }
      }

      {
        throw new UnknownLine(line, place);
      }

    }
  }

}
