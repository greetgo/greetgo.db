package kz.greetgo.gbatis2.struct;

import kz.greetgo.gbatis2.struct.exceptions.*;
import kz.greetgo.gbatis2.struct.model.DbEssence;
import kz.greetgo.gbatis2.struct.model.DbField;
import kz.greetgo.gbatis2.struct.model.EnumEssence;
import kz.greetgo.gbatis2.struct.model.Essence;
import kz.greetgo.gbatis2.struct.model.std.StdEssence;
import kz.greetgo.gbatis2.struct.model.std.StdEssenceFactory;

import java.util.*;

import static kz.greetgo.util.ServerUtil.fnn;

public class DbStruct {

  private final StdEssenceFactory stdFactory = new StdEssenceFactory();

  public final Options options = new Options();

  final Aliases aliases = new Aliases();

  final Enums enums = new Enums();

  final Map<String, ParsedEssence> essenceMap = new HashMap<>();

  public final List<DbEssence> dbEssenceList = new ArrayList<>();

  void prepareDbEssenceList() {

    checkAliasesOverlaps();
    checkEnumOverlapStdType();

    final List<String> keyList = new ArrayList<>();
    keyList.addAll(essenceMap.keySet());
    Collections.sort(keyList);

    for (String key : keyList) {
      ParsedEssence pe = essenceMap.get(key);
      pe.dbEssence = new DbEssence(pe.subpackage, pe.name, pe.comment);
      dbEssenceList.add(pe.dbEssence);
    }

    for (ParsedEssence parsedEssence : essenceMap.values()) {
      if (parsedEssence.type != null) {
        Essence keyEssence = findEssenceForWrittenType(parsedEssence.type, parsedEssence.place);

        parsedEssence.dbEssence.fieldList.add(
            new DbField(true, parsedEssence.name, keyEssence, "Key of " + parsedEssence.comment)
        );
        parsedEssence.dbEssence.keyFromEssence = true;
      }
      for (ParsedField parsedField : parsedEssence.fieldList) {
        DbField field = createDbField(parsedField);

        if (field.key && parsedEssence.dbEssence.keyFromEssence) throw new MustNotBeUsedTwoWaysOfDefiningKeyFields(
            parsedEssence.dbEssence.name, parsedField.name, parsedField.place
        );

        parsedEssence.dbEssence.fieldList.add(field);
      }
    }

  }

  private void checkEnumOverlapStdType() {
    for (EnumDot enumDot : enums.enumMap.values()) {

      if (stdFactory.parse(enumDot.name) != null) {
        throw new EnumOverlapStdType(enumDot);
      }

    }
  }

  private void checkAliasesOverlaps() {
    for (AliasDot aliasDot : aliases.aliasMap.values()) {

      if (stdFactory.parse(aliasDot.name) != null) {
        throw new AliasOverlapStdType(aliasDot);
      }

      {
        EnumDot enumDot = enums.enumMap.get(aliasDot.name);
        if (enumDot != null) {
          throw new AliasOverlapEnum(aliasDot, enumDot);
        }
      }

      {
        ParsedEssence parsedEssence = essenceMap.get(aliasDot.name);
        if (parsedEssence != null) {
          throw new AliasOverlapEssence(aliasDot, parsedEssence);
        }
      }

    }
  }

  private DbField createDbField(ParsedField parsedField) {
    Essence essence = findEssence(parsedField);
    return new DbField(parsedField.key, parsedField.name, essence, parsedField.comment);
  }

  private Essence findEssence(ParsedField parsedField) {

    String writtenType = fnn(parsedField.type, parsedField.name);

    return findEssenceForWrittenType(writtenType, parsedField.place);
  }

  private Essence findEssenceForWrittenType(String writtenType, Place place) {
    String type = aliases.real(writtenType);

    {
      EnumEssence enumEssence = enums.take(type);
      if (enumEssence != null) return enumEssence;
    }

    {
      StdEssence stdEssence = stdFactory.parse(type);
      if (stdEssence != null) return stdEssence;
    }

    {
      DbEssence dbEssence = takeEssence(type);
      if (dbEssence != null) return dbEssence;
    }

    throw new NoEssence(type, writtenType, place);
  }

  private DbEssence takeEssence(String type) {
    ParsedEssence parsedEssence = essenceMap.get(type);
    if (parsedEssence == null) return null;
    return parsedEssence.dbEssence;
  }

}
