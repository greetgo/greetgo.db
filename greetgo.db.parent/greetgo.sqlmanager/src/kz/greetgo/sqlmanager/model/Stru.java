package kz.greetgo.sqlmanager.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранит структуру данных в NF3-нотации
 * 
 * @author pompei
 * 
 */
public class Stru {
  public final Map<String, Table> tables = new HashMap<>();
  
  public Map<String, String> tableComment, fieldComment;
}
