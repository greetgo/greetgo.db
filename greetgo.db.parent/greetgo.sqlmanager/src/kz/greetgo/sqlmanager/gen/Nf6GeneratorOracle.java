package kz.greetgo.sqlmanager.gen;

import java.io.PrintStream;

import kz.greetgo.sqlmanager.gen.procedures.Changer;
import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.FieldDb;
import kz.greetgo.sqlmanager.model.Table;
import kz.greetgo.sqlmanager.parser.StruShaper;

public class Nf6GeneratorOracle extends Nf6Generator {
  
  private SqlDialect sqlDialect = null;
  
  @Override
  protected SqlDialect dialect() {
    if (sqlDialect == null) sqlDialect = new SqlDialectOracle();
    return sqlDialect;
  }
  
  public Nf6GeneratorOracle(Conf conf, StruShaper sg) {
    super(conf, sg);
  }
  
  @Override
  protected void printTableInsertFunction(PrintStream out, Table table) {
    String fname = conf._p_ + table.name;
    out.print("create or replace procedure " + fname + "(");
    
    {
      boolean first = true;
      for (Field key : table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "" :", ");
          first = false;
          out.print(fi.name + "__ " + dialect().procType(fi.stype));
        }
      }
    }
    
    out.println(") is begin");
    
    {
      out.print("  insert into " + conf.tabPrefix + table.name + " (");
      {
        boolean first = true;
        for (Field key : table.keys) {
          for (FieldDb fi : key.dbFields()) {
            out.print(first ? "" :", ");
            first = false;
            out.print(fi.name);
          }
        }
      }
      
      if (conf.userIdFieldType != null && conf.createdBy != null) {
        out.print(", " + conf.createdBy);
      }
      
      out.print(") values (");
      {
        boolean first = true;
        for (Field key : table.keys) {
          for (FieldDb fi : key.dbFields()) {
            out.print(first ? "" :", ");
            first = false;
            out.print(fi.name + "__");
          }
        }
      }
      
      if (conf.userIdFieldType != null && conf.createdBy != null) {
        out.print(", " + conf.get_changer + "()");
      }
      
      out.println(") ; ");
    }
    
    out.println("exception when dup_val_on_index then null ; ");
    out.println("end ; ");
    
    out.println(conf.separator);
  }
  
  @Override
  protected void printFieldInsertFunction(PrintStream out, Field field) {
    
    String fname = conf._p_ + field.table.name + '_' + field.name;
    out.print("create or replace procedure " + fname + "(");
    
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "" :", ");
          first = false;
          out.print(fi.name + "__ " + dialect().procType(fi.stype));
        }
      }
      for (FieldDb fi : field.dbFields()) {
        out.print(", " + fi.name + "__ " + dialect().procType(fi.stype));
      }
    }
    
    out.println(") is");
    
    String tname = conf.tabPrefix + field.table.name + "_" + field.name;
    
    out.println("  r " + tname + "%rowtype ; ");
    out.println("  doit int ; ");
    out.println("begin");
    
    out.print("  select case when exists (select ");
    if (conf.useIndexHintForOracle) {
      out.print(" /*+ INDEX(" + tname + ") */ ");
    }
    out.println("1 from " + tname);
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "    where " :"    and ");
          out.println(fi.name + " = " + fi.name + "__");
          first = false;
        }
      }
    }
    out.println("    ) then 0 else 1 end into doit from dual ; ");
    
    out.println();
    out.println("  if doit = 0 then ");
    out.println();
    
    out.println("  select * into r");
    out.println("    from " + tname);
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "    where " :"    and ");
          out.println(fi.name + " = " + fi.name + "__");
          first = false;
        }
      }
    }
    out.println("    and " + conf.ts + " = (select max(" + conf.ts + ") ");
    out.println("      from " + tname);
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          out.print(first ? "      where " :"      and ");
          out.println(fi.name + " = " + fi.name + "__");
          first = false;
        }
      }
    }
    out.println("      ) ; ");
    out.println();
    
    for (FieldDb fi : field.dbFields()) {
      out.println("  if doit = 0 and " + fi.name + "__ is null and r." + fi.name
          + " is not null then doit := 1 ; end if ; ");
      out.println("  if doit = 0 and " + fi.name + "__ is not null and r." + fi.name
          + " is null then doit := 1 ; end if ; ");
      out.println("  if doit = 0 and " + fi.name + "__ is not null and r." + fi.name
          + " is not null and " + fi.name + "__ != r." + fi.name + " then doit := 1 ; end if ; ");
      out.println();
    }
    
    out.println("  end if ; ");
    out.println();
    
    out.println("  if doit = 1 then ");
    printInsertSql(out, tname, field, conf.modi);
    out.println("  end if ; ");
    
    if (conf.genOperTables) {
      out.println();
      String oname = conf.oPref + field.table.name + "_" + field.name;
      out.print("  select count(1) into doit from " + oname);
      {
        printWhereSqlForKeys(out, field);
      }
      out.println(" ; ");
      out.println("  if doit = 0 then");
      printInsertSql(out, oname, field, conf.insertedBy, conf.lastModifiedBy);
      out.println("  else");
      printUpdateSql(out, oname, field);
      out.println("  end if ; ");
    }
    
    out.println("end ; ");
    out.println(conf.separator);
  }
  
  @Override
  protected void printPrepareSqls(PrintStream out) {
    out.println("create or replace function moment return timestamp");
    out.println("is begin return systimestamp ; end ; ");
    out.println(conf.separator);
    out.println();
    
    if (conf.userIdFieldType != null) {
      for (String sql : Changer.forOracle(conf.userIdFieldType)) {
        sql = sql.replaceAll("__GET_CHANGER__", conf.get_changer);
        sql = sql.replaceAll("__SET_CHANGER__", conf.set_changer);
        out.print(sql);
        out.println();
        out.println(conf.separator);
        out.println();
      }
    }
    
  }
  
  @Override
  protected String minus() {
    return "minus";
  }
  
  @Override
  protected void
      printInsertSql(PrintStream out, String tname, Field field, String... changerFields) {
    if (conf.oracleInsertDupValues) {
      out.println("    begin");
      printInsertCommand("    ", out, tname, field, changerFields);
      out.println("    exception when dup_val_on_index then");
      out.println("      dbms_lock.sleep( 0.005 ) ; ");
      printInsertCommand("    ", out, tname, field, changerFields);
      out.println("    end ; ");
    } else {
      printInsertCommand("      ", out, tname, field, changerFields);
    }
  }
}
