package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Определяет вьюшку в 3-ей нормальной форме (NF3), по имени базовой таблицы из
 * 6-ой нормальной формы (NF6).
 * 
 * @author pompei
 */
@Documented
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface T1 {
  /**
   * Ипределает имя базовой таблицы в NF6
   * 
   * @return имя таблицы содержащее вначале префикс таблицы
   */
  String value();
  
  /**
   * Определяет список полей, которые будут формировать вьюшку
   * 
   * @return список полей. Если список пустой, то будут использоваться все поля
   */
  String[] fields() default {};
  
  /**
   * Определяет имя результирующей вьюшки
   * 
   * @return имя результирующей вьюшки. Если пуская строка, то имя определяеться
   *         автоматически из имени базовой таблицы путём замены префикса на
   *         префикс встраиваемой вьюшки
   */
  String name() default "";
  
}
