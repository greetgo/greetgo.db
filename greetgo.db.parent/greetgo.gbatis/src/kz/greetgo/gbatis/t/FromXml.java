package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Показывает, что SQL берёться из xml-а
 * 
 * @author pompei
 */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromXml {
  /**
   * Определяет идентификатор метода
   * 
   * @return идентификатор метода
   */
  String value();
  
  /**
   * Определяет положение xml-файла, от куда берёться SQL
   * 
   * @return положение xml-файла
   */
  String xml() default "";
}
