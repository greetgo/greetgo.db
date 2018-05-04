package kz.greetgo.gbatis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Инкапулирует в себе информацию о запросе к БД
 * 
 * @author pompei
 */
public class Request {
  /**
   * SQL с параметрами в форме GBatis (этот SQL ещё надо обрадотать, заменив GBatis параметры на
   * значения или ?)
   */
  public String sql;
  /**
   * Тип запроса
   */
  public RequestType type;
  /**
   * Список предварительных селектов для конструкции With
   */
  public final List<WithView> withList = new ArrayList<>();
  /**
   * Список параметров со значениями для GBatis параметров SQL-я
   */
  public final List<Param> paramList = new ArrayList<>();
  
  /**
   * Информация о результате
   */
  public final Result result = new Result();
  
  /**
   * Признак немедленного выполнения запроса.
   * <p>
   * Если равен <code>false</code>, то запрос не выполняется, а возвращается объект реализующий
   * {@link FutureCall}, иначе немедленно вызывается метод {@link FutureCall#last()}
   * </p>
   */
  public boolean callNow;
  
}
