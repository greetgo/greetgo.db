package kz.greetgo.gbatis.classscanner;

import java.util.Set;

/**
 * Сканер пакетов для загрузки классов
 * 
 * @author pompei
 */
public interface ClassScanner {
  /**
   * Сканирует пакет, и возвращает список полученных, при этом, классов
   * 
   * @param packageName
   *          сканируемый пакет
   * @return список отсканированных классов
   */
  Set<Class<?>> scanPackage(String packageName);
}
