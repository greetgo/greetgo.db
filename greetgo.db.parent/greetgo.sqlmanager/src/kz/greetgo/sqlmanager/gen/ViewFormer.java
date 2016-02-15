package kz.greetgo.sqlmanager.gen;

import kz.greetgo.sqlmanager.model.Field;
import kz.greetgo.sqlmanager.model.Table;

/**
 * Формирователь вьюшек доступа к NF3 данным
 * 
 * <p>
 * Дело в том, что преобразование из NF6 в NF3 можно делать по разному. И с помощью этого интерфейсы
 * алгоритмы преобразования из NF6 в NF3 вынесены отдельно
 * </p>
 * 
 * @author pompei
 */
public interface ViewFormer {
  /**
   * Формирование SQL для доступа к полю
   * 
   * @param sb
   *          место, куда формируется SQL
   * @param field
   *          поле, доступ к которому формируется в этом методе
   * @param time
   *          таблица и поле времени, в котором содержится момент, на который надо получать данные.
   *          Если <code>null</code>, то SQL-формируется для доступа к последним данным
   *          <p>
   *          <i> Эта таблица должна быть предопределена ранее в with-выражении</i>
   *          </p>
   * @param tabSize
   *          размер отступа справа (для получения красивого SQL-я) (если = 0, то SQL-формируется в
   *          одну строку)
   * @param orig
   *          сдвиг всего SQL-выражения в tabSize-ах (для получения красивого SQL-я)
   */
  void formFieldSelect(StringBuilder sb, Field field, String time, int tabSize, int orig);
  
  /**
   * Формирование SQL для доступа ко всей псевдотаблицы в представлении NF3
   * 
   * @param sb
   *          место, куда формируется SQL
   * @param table
   *          таблица, доступ к которой формируется в этом методе
   * @param time
   *          таблица и поле времени, в котором содержится момент, на который надо получать данные.
   *          Если <code>null</code>, то SQL-формируется для доступа к последним данным
   *          <p>
   *          <i> Эта таблица должна быть предопределена ранее в with-выражении</i>
   *          </p>
   * @param tabSize
   *          размер отступа справа (для получения красивого SQL-я) (если = 0, то SQL-формируется в
   *          одну строку)
   * @param orig
   *          сдвиг всего SQL-выражения в tabSize-ах (для получения красивого SQL-я)
   */
  void formTableSelect(StringBuilder sb, Table table, String time, int tabSize, int orig);
}
