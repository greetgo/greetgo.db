package kz.greetgo.gbatis.modelreader;

import java.util.HashMap;
import java.util.Map;

/**
 * Содержит DOM отсканированного xml-а для запроса
 * 
 * @author pompei
 */
public class XmlContent {
  /**
   * Список данных запросов из отсканированного xml-а
   */
  public final Map<String, XmlRequest> requestMap = new HashMap<>();
}
