package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see T1
 * 
 * @author pompei
 */
@Documented
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface T2 {
  /**
   * @see T1#value()
   */
  String value();
  
  /**
   * @see T1#fields()
   */
  String[] fields() default {};
  
  /**
   * @see T1#name()
   */
  String name() default "";
}
