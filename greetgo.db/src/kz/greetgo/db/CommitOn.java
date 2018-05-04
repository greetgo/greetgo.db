package kz.greetgo.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * По умолчанию, в режиме транзакций, если возникло усключение, то происходит rollback.
 * С помощью этой аннотации можно указать исключения, при которых будет вызываться commit
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommitOn {
  /**
   * Спискок исключений, при которых будет вызываться commit, при вызове этого метода,
   * или при вызове методов этого класса
   *
   * @return сптсок классов
   */
  Class<? extends Throwable>[] value();
}
