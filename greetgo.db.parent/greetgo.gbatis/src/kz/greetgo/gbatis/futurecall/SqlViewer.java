package kz.greetgo.gbatis.futurecall;

import java.util.List;

/**
 * Интерфейс для получения выполняемых SQL-ей
 * 
 * <p>
 * Данный интерфейс можно использовать для логирования работа с БД
 * <p>
 * 
 * @author pompei
 */
public interface SqlViewer {
  /**
   * Получения признака необходимости получения выполняемого SQL-я
   * 
   * @return признак необходимости получения выполняемого SQL-я
   */
  boolean needView();
  
  /**
   * Вызывается после выполнения SQL-я (или при ошибке вызова), но только в том случае, если метод
   * {@link #needView()} вернул <code>true</code>
   * 
   * @param sql
   *          выполняемый SQL
   * @param params
   *          список значений, подставляемых на места (?) в выполняемом SQL-е
   * @param delay
   *          время выполнения запроса
   * @param err
   *          ошибка выполнения SQL-я, или null, если вызов был удачен
   */
  void view(String sql, List<Object> params, long delay, Exception err);
}
