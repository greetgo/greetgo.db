package kz.greetgo.gbatis.modelreader;

import java.util.ArrayList;
import java.util.List;

import kz.greetgo.gbatis.model.RequestType;
import kz.greetgo.gbatis.model.WithView;

/**
 * Данные запроса из отсканированного xml-а
 * 
 * @author pompei
 */
public class XmlRequest {
  /**
   * Идентификатор запроса
   */
  public String id;
  
  /**
   * Тип запроса
   */
  public RequestType type;
  /**
   * Список вьюшек для конструкции With
   */
  public final List<WithView> withViewList = new ArrayList<>();
  
  /**
   * SQL-запроса с параметрами типа GBatis
   */
  public String sql;
  
  @Override
  public String toString() {
    return "XmlRequest " + id + " " + type + " [" + sql + "] " + withViewList;
  }
}
