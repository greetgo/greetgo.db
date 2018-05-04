package kz.greetgo.db;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InTransaction {
  IsolationLevel value() default IsolationLevel.DEFAULT;
}
