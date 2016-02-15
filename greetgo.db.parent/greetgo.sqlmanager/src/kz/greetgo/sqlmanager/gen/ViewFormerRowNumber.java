package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.FieldDb;

/**
 * Формирователь вьюшек доступа к NF3 данным посредством использования функции
 * <code>row_number()</code>
 * 
 * @see ViewFormer
 * @author pompei
 * 
 */
public class ViewFormerRowNumber extends ViewFormerMax {
  
  protected ViewFormerRowNumber(Conf conf) {
    super(conf);
  }
  
  @Override
  public void formFieldSelect(StringBuilder sb, Field field, String time, int tabSize, int orig) {
    String s0 = " ", s1 = s0, nl = "";
    if (tabSize > 0) {
      s0 = space(tabSize, orig + 0);
      s1 = space(tabSize, orig + 1);
      nl = "\n";
    }
    
    sb.append(s0 + "select ");
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          sb.append(first ? "" :", ");
          first = false;
          sb.append(fi.name);
        }
      }
      for (FieldDb fi : field.dbFields()) {
        sb.append(", " + fi.name);
      }
    }
    sb.append(" from (" + nl);
    
    sb.append(s1 + "select m.*, row_number() over (partition by ");
    {
      boolean first = true;
      for (Field key : field.table.keys) {
        for (FieldDb fi : key.dbFields()) {
          sb.append(first ? "" :", ");
          first = false;
          sb.append("m." + fi.name);
        }
      }
    }
    String tname = conf.tabPrefix + field.table.name + "_" + field.name;
    sb.append(" order by m." + conf.ts + " desc ) as rn__ from " + tname + " m" + nl);
    
    if (time != null && time.trim().length() > 0) {
      String T = "", TF = "";//T - table, TF - table.field
      int idx = time.indexOf('.');
      if (idx < 0) {
        T = time.trim();
        TF = T + '.' + T;
      } else {
        T = time.substring(0, idx).trim();
        TF = time.trim();
      }
      sb.append(s1 + ", " + T + " where m." + conf.ts + " <= " + TF + nl);
    }
    
    sb.append(s0 + ") x where x.rn__ = 1");
    
  }
  
}
