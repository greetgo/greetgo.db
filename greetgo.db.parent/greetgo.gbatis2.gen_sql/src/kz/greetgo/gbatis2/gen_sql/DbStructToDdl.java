package kz.greetgo.gbatis2.gen_sql;

import kz.greetgo.gbatis2.gen_sql.ddl.*;
import kz.greetgo.gbatis2.struct.DbStruct;
import kz.greetgo.gbatis2.struct.model.DbEssence;
import kz.greetgo.gbatis2.struct.model.KeyField;
import kz.greetgo.gbatis2.struct.model.std.StdEssence;
import kz.greetgo.gbatis2.struct.model.std.StdStr;
import kz.greetgo.gbatis2.struct.model.std.StdTime;

import java.util.ArrayList;
import java.util.List;

public class DbStructToDdl {

  public StdEssence userFieldType = new StdStr(30);

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
  }

  private void generateOperativeTables() {

  }

  private void generateMemoryTables() {

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

}
