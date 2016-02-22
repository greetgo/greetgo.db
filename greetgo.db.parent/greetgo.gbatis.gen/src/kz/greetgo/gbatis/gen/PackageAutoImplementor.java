package kz.greetgo.gbatis.gen;

import kz.greetgo.class_scanner.ClassScanner;
import kz.greetgo.class_scanner.ClassScannerDef;
import kz.greetgo.gbatis.t.Autoimpl;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageAutoImplementor {

  public static List<GenResult> autoImplementPackage(AutoImplementor autoImplementor, String packageName) throws Exception {
    final ClassScanner classScanner = new ClassScannerDef();
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    List<GenResult> ret = new ArrayList<>();

    for (Class<?> aClass : classScanner.scanPackage(packageName)) {
      if (aClass.getAnnotation(Autoimpl.class) == null) continue;
      final GenResult genResult = autoImplementor.generate(aClass);
      ret.add(genResult);

      final String fileName = genResult.javaFile().toString();
      final File classFile = new File(fileName.substring(0, fileName.lastIndexOf('.')) + ".class");
      boolean needCompile = true;

      if (genResult.hashFile().exists() && classFile.exists()) {
        if (classFile.lastModified() >= genResult.hashFile().lastModified()) {
          needCompile = false;
        }
      }

      if (needCompile) {
        final int exitCode = compiler.run(System.in, System.out, System.err, genResult.javaFile().getPath());
        if (exitCode != 0) throw new RuntimeException("exitCode = " + exitCode);
      }
    }

    return ret;
  }

}
