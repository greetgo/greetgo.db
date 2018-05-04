package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Определяет, что запрос вызывает хранимую функцию
 * 
 * @author pompei
 */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Call {
  /**
   * Определяет SQL-запрос на вызов хранимой функции
   * 
   * @return SQL-запрос
   */
  String value();
}
