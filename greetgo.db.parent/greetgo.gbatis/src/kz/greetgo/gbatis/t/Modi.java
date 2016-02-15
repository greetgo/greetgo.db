package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Определяет SQL запрос
 * 
 * @author pompei
 */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Modi {
  /**
   * Определяет текст SQL-запроса
   * 
   * @return имя параметра
   */
  String value();
}
