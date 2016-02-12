package kz.greetgo.db;

import java.lang.annotation.*;

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
   */
  Class<? extends Throwable>[] value();
}
