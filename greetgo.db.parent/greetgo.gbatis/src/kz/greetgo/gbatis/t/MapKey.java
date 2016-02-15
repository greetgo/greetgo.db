package kz.greetgo.gbatis.t;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kz.greetgo.gbatis.model.ResultType;

/**
 * Определяет имя поля, которое будет использоваться в качестве ключа в
 * резйльтирующем мапе. Используеться совместно с {@link ResultType#MAP}
 * 
 * @author pompei
 */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapKey {
  /**
   * Определяет местоположение xml-а. Если пустой, то используеться имя = <nobr>
   * &lt;имя класса&gt;.xml</nobr>
   * 
   * @return меcтоположение xml-а
   */
  String value();
}
