package kz.greetgo.db.nf36.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Nf3GenerateHistorySelector {
  String atMethodName() default "at";

  String toSuffix() default "To";
}
