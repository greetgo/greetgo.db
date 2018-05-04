package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Определяет имя параметра
 * 
 * @author pompei
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Prm {
  /**
   * Определяет имя параметра
   * 
   * @return имя параметра
   */
  String value();
}
