package kz.greetgo.gbatis.classscanner;

import java.util.Set;

import com.metapossum.utils.scanner.reflect.ClassesInPackageScanner;

/**
 * Реализация сканера по умолчанию
 * 
 * @author pompei
 */
public class ClassScannerDef implements ClassScanner {
  
  @Override
  public Set<Class<?>> scanPackage(final String packageName) {
    try {
      return new ClassesInPackageScanner().scan(packageName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
