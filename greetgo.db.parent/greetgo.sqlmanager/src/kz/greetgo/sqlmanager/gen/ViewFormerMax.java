package kz.greetgo.sqlmanager.gen;

import java.util.ArrayList;
import java.util.List;

import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.SimpleType;
import kz.greetgo.sqlmanager.model.Table;

/**
 * Формирователь вьюшек доступа к NF3 данным посредством использования функции <code>max</code>
 * 
 * @see ViewFormer
 * @author pompei
 * 
 */
public class ViewFormerMax extends ViewFormerAbstract {
  
  protected ViewFormerMax(Conf conf) {
    super(conf);
  }
  
  @Override
  public void formFieldSelect(StringBuilder sb, Field field, String time, int tabSize, int orig) {
    String s0 = " ", s1 = s0, s2 = s0, nl = "";
    if (tabSize > 0) {
      s0 = space(tabSize, orig + 0);
      s1 = space(tabSize, orig + 1);
      s2 = space(tabSize, orig + 2);
      nl = "\n";
    }
    
    String T = "", TF = "";//T - table, TF - table.field
    
    final String fieldTableTS;
    if (time == null || time.trim().length() == 0) {
      fieldTableTS = conf.tabPrefix + field.table.name + "_" + field.name;
    } else {
      int idx = time.indexOf('.');
      if (idx < 0) {
        T = time;
        TF = time + '.' + time;
      } else {
        T = time.substring(0, idx);
        TF = time;
      }
      fieldTableTS = "(select x.* from " + conf.tabPrefix + field.table.name + "_" + field.name
          + " x, " + T + " where x." + conf.ts + " <= " + TF + ')';
    }
    
    List<String> keyNames = field.table.keyNames();
    
    List<String> names = new ArrayList<>();
    
    List<SimpleType> types = new ArrayList<>();
    field.type.assignSimpleTypes(types);
    if (types.size() == 1) {
      names.add(field.name);
    } else
      for (int i = 1, C = types.size(); i <= C; i++) {
        names.add(field.name + i);
      }
    
    sb.append(s0 + "select" + nl);
    {
      boolean first = true;
      for (String tname : keyNames) {
        String k = first ? " " :",";
        first = false;
        sb.append(s1 + k + "aa." + tname + nl);
      }
    }
    for (String name : names) {
      sb.append(s1 + ",bb." + name + nl);
    }
    sb.append(s0 + "from (" + nl);
    sb.append(s1 + "select" + nl);
    for (String tname : keyNames) {
      sb.append(s2 + "a." + tname + "," + nl);
    }
    sb.append(s2 + "max(b." + conf.ts + ") ts" + nl);
    sb.append(s1 + "from " + conf.tabPrefix + field.table.name + " a" + nl);
    sb.append(s1 + "left join " + fieldTableTS + " b" + nl);
    for (int i = 0, C = keyNames.size(); i < C; i++) {
      sb.append(s1).append(i == 0 ? "on" :"and");
      sb.append(" a." + keyNames.get(i) + " = b." + keyNames.get(i) + nl);
    }
    if (time != null && time.trim().length() > 0) {
      sb.append(s1 + "," + T + " where b." + conf.ts + " <= " + time + nl);
    }
    sb.append(s1 + "group by" + nl);
    for (int i = 0, C = keyNames.size(); i < C; i++) {
      String k = i == 0 ? " " :",";
      sb.append(s2 + k + "a." + keyNames.get(i) + nl);
    }
    sb.append(s0 + ") aa left join " + conf.tabPrefix + field.table.name + "_" + field.name + " bb"
        + nl);
    for (int i = 0, C = keyNames.size(); i < C; i++) {
      String tname = keyNames.get(i);
      String fname = keyNames.get(i);
      String and = i == 0 ? "on" :"and";
      sb.append(s0 + and + " aa." + tname + " = bb." + fname + nl);
    }
    sb.append(s0 + "and aa." + conf.ts + " = bb." + conf.ts);
  }
  
  @Override
  public void formTableSelect(StringBuilder sb, Table table, String time, int tabSize, int orig) {
    String s0 = " ", s1 = s0, s2 = s0, nl = "";
    if (tabSize > 0) {
      s0 = space(tabSize, orig + 0);
      s1 = space(tabSize, orig + 1);
      s2 = space(tabSize, orig + 2);
      nl = "\n";
    }
    String T = null, TF = null;//T - table, TF - table.field
    if (time != null && time.trim().length() > 0) {
      int idx = time.indexOf('.');
      if (idx < 0) {
        T = time;
        TF = time + '.' + time;
      } else {
        T = time.substring(0, idx);
        TF = time;
      }
    }
    
    List<String> keyNames = table.keyNames();
    
    sb.append(s0 + "select" + nl);
    for (String fname : keyNames) {
      sb.append(s1 + " x." + fname + ',' + nl);
    }
    sb.append(s1 + " x." + conf.cre + nl);
    {
      int i = 1;
      for (Field field : table.fields) {
        List<SimpleType> types = new ArrayList<>();
        field.type.assignSimpleTypes(types);
        if (types.size() == 1) {
          sb.append(s1 + ",x" + i + "." + field.name + nl);
        } else
          for (int j = 1, C = types.size(); j <= C; j++) {
            sb.append(s1 + ",x" + i + "." + field.name + j + nl);
          }
        i++;
      }
    }
    {
      sb.append(s0 + "from ");
      String t = conf.tabPrefix + table.name;
      if (T == null) {
        sb.append(t);
      } else {
        sb.append("(select u.* from " + t + " u, " + T + " where u." + conf.cre + " <= " + TF + ")");
      }
      sb.append(" x" + nl);
    }
    {
      int i = 1;
      for (Field field : table.fields) {
        sb.append(s0 + "left join" + nl);
        if (T == null) {
          sb.append(s2 + conf.vPrefix + table.name + "_" + field.name + nl);
        } else {
          sb.append('(');
          formFieldSelect(sb, field, time, tabSize, orig + 2);
          sb.append(')');
        }
        boolean first = true;
        for (String key : keyNames) {
          sb.append(s1).append(first ? "x" + i + " on " :"and ");
          first = false;
          sb.append("x" + i + "." + key + " = x." + key + nl);
        }
        i++;
      }
    }
  }
}
