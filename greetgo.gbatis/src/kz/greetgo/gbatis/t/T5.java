package kz.greetgo.gbatis.t;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * @author pompei
 * @see T1
 */
@Documented
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface T5 {
  String value();

  String[] fields() default {};

  String name() default "";
}
