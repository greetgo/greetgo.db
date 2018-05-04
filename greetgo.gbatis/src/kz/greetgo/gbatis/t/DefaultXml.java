package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Определяет метоположение xml-а с SQL-ем по умолчанию
 * 
 * @author pompei
 */
@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultXml {
  /**
   * Определяет метоположение xml-а с SQL-ем по умолчанию
   * 
   * @return метоположение xml-а с SQL-ем
   */
  String value();
}
