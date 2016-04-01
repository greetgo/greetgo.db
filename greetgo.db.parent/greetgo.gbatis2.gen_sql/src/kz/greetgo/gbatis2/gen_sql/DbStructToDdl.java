package kz.greetgo.gbatis2.gen_sql;

import kz.greetgo.gbatis2.gen_sql.ddl.*;
import kz.greetgo.gbatis2.struct.DbStruct;
import kz.greetgo.gbatis2.struct.model.DbEssence;
import kz.greetgo.gbatis2.struct.model.DbField;
import kz.greetgo.gbatis2.struct.model.KeyField;
import kz.greetgo.gbatis2.struct.model.SimpleEssence;
import kz.greetgo.gbatis2.struct.model.std.StdEssence;
import kz.greetgo.gbatis2.struct.model.std.StdStr;
import kz.greetgo.gbatis2.struct.model.std.StdTime;

import java.util.ArrayList;
import java.util.List;

public class DbStructToDdl {

  public StdEssence userFieldType = new StdStr(30);

  public boolean useOperativeTables = true;
  public boolean addUserFields = true;

  private final DbStruct dbStruct;

  public final List<Ddl> ddlList = new ArrayList<>();

  public DbStructToDdl(DbStruct dbStruct) {
    this.dbStruct = dbStruct;
  }

  public void generateDDL() throws Exception {
    generateSequences();
    generateKeyTables();
    generateMemoryTables();
    generateOperativeTables();
    generateForeignKeys();
  }


  private String sequencePrefix() {
    return dbStruct.options.sequencePrefix();
  }

  private String tablePrefixId() {
    return dbStruct.options.tablePrefixId();
  }

  private void generateSequences() throws Exception {
    for (DbEssence dbEssence : dbStruct.dbEssenceList) {
      if (dbEssence.isSequential()) {
        ddlList.add(new CreateSequence(sequencePrefix() + dbEssence.name));
      }
    }
  }

  private void generateKeyTables() {
    for (DbEssence dbEssence : dbStruct.dbEssenceList) {
      generateKeyTable(dbEssence);
    }
  }

  private void generateKeyTable(DbEssence dbEssence) {

    CreateTable createTable = new CreateTable(tablePrefixId() + dbEssence.name);
    for (KeyField keyField : dbEssence.keyFields()) {
      createTable.fields.add(new CreateTableField(keyField.name(), keyField.type(), true, null));
      createTable.primaryKey.add(keyField.name());
    }

    createTable.fields.add(new CreateTableField(dbStruct.options.keyTableCreatedAt(),
        StdTime.INSTANCE, true, DefaultCurrentTimestamp.INSTANCE));

    if (userFieldType != null) {
      createTable.fields.add(new CreateTableField(dbStruct.options.keyTableCreatedBy(), userFieldType, false, null));
    }

    ddlList.add(createTable);
  }

  private void generateMemoryTables() {
    for (DbEssence dbEssence : dbStruct.dbEssenceList) {
      generateMemoryTable(dbEssence);
    }
  }

  private void generateMemoryTable(DbEssence dbEssence) {

    List<KeyField> keyFields = dbEssence.keyFields();

    for (DbField dbField : dbEssence.fieldList) {
      if (dbField.key) continue;
      generateMemoryTableFor(dbEssence, keyFields, dbField);
    }

  }

  private void generateMemoryTableFor(DbEssence dbEssence, List<KeyField> keyFields, DbField dbField) {

    CreateTable createTable = new CreateTable(dbStruct.options.tablePrefixMemory()
        + dbEssence.name + dbStruct.options.concatenationSuffix() + dbField.name);
    ddlList.add(createTable);

    for (KeyField keyField : keyFields) {
      createTable.addNewField(keyField.name(), keyField.type(), true, null);
      createTable.addPrimaryKeyName(keyField.name());
    }

    {
      createTable.addNewField(dbStruct.options.tsFieldName(), StdTime.INSTANCE, true, null);
      createTable.addPrimaryKeyName(dbStruct.options.tsFieldName());
    }

    List<SimpleEssence> typeList = dbField.type.simpleEssenceList();
    int index = 0, size = typeList.size();
    if (size == 0) throw new RuntimeException("simpleEssenceList.size == 0");
    for (SimpleEssence simpleEssence : typeList) {
      index++;
      String name = dbField.name;
      if (size > 1) name += index;
      createTable.addNewField(name, simpleEssence, false, null);
    }

  }

  private void generateOperativeTables() {
    if (!useOperativeTables) return;

    for (DbEssence dbEssence : dbStruct.dbEssenceList) {
      generateOperativeTable(dbEssence);
    }
  }

  private void generateOperativeTable(DbEssence dbEssence) {
    List<KeyField> keyFields = dbEssence.keyFields();

    for (DbField dbField : dbEssence.fieldList) {
      if (dbField.key) continue;
      generateOperativeTableFor(dbEssence, keyFields, dbField);
    }
  }

  private void generateOperativeTableFor(DbEssence dbEssence, List<KeyField> keyFields, DbField dbField) {
    CreateTable createTable = new CreateTable(dbStruct.options.tablePrefixOperative()
        + dbEssence.name + dbStruct.options.concatenationSuffix() + dbField.name);
    ddlList.add(createTable);

    for (KeyField keyField : keyFields) {
      createTable.addNewField(keyField.name(), keyField.type(), true, null);
      createTable.addPrimaryKeyName(keyField.name());
    }

    List<SimpleEssence> typeList = dbField.type.simpleEssenceList();
    int index = 1, size = typeList.size();
    if (size == 0) throw new RuntimeException("simpleEssenceList.size == 0");
    for (SimpleEssence simpleEssence : typeList) {
      String name = dbField.name;
      if (size > 1) name += index;
      createTable.addNewField(name, simpleEssence, false, null);
      index++;
    }
  }

  private void generateForeignKeys() {
    for (DbEssence dbEssence : dbStruct.dbEssenceList) {
      generateForeignKeysForEssence(dbEssence);
    }
  }

  private void generateForeignKeysForEssence(DbEssence dbEssence) {

    List<KeyField> keyFields = dbEssence.keyFields();

    generateMainForeignKeyFor(dbEssence, keyFields);
    
  }

  private void generateMainForeignKeyFor(DbEssence dbEssence, List<KeyField> keyFields) {
    
  }
}
