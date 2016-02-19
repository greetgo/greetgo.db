package kz.greetgo.gbatis.gen;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.gbatis.futurecall.FutureCallImpl;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.modelreader.RequestGenerator;
import kz.greetgo.gbatis.t.DbAccessInfo;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static kz.greetgo.gbatis.gen.CodeUtil.classNameToCodeName;
import static kz.greetgo.gbatis.gen.CodeUtil.typeToCode;
import static kz.greetgo.util.ServerUtil.dummyCheck;
import static kz.greetgo.util.ServerUtil.extractName;

public class AutoImplementor {
  private final String srcDir;
  private final DbAccessInfo dbAccess;

  public AutoImplementor(String srcDir, DbAccessInfo dbAccess) {
    this.srcDir = srcDir;
    this.dbAccess = dbAccess;
  }

  private static final String UNIQUE_SUFFIX = "74832914509189";

  public GenResult generate(Class<?> autoImplInterface) throws Exception {
    final String implClassName = autoImplInterface.getName() + "AutoImplemented" + UNIQUE_SUFFIX;

    final File file = new File(srcDir + '/' + implClassName.replace('.', '/') + ".java");

    dummyCheck(file.getParentFile().mkdirs());

    final String requestClassName = Request.class.getName();
    final String fcName = FutureCallImpl.class.getName();
    final String bgName = BeanGetter.class.getName();


    try (final PrintWriter out = new PrintWriter(file, "UTF-8")) {
      autoImplInterface.getPackage().getName();
      out.println("package " + autoImplInterface.getPackage().getName() + ";");
      out.println();

      out.println("@" + Bean.class.getName());
      out.println("public class " + extractName(implClassName) + " implements " + autoImplInterface.getName() + " {");

      out.println("  public " + bgName + "<" + SqlViewer.class.getName() + "> sqlViewer;");
      out.println("  public " + bgName + "<" + classNameToCodeName(dbAccess.getClass().getName()) + "> dbAccess;");

      int methodIndex = 0;

      for (Method method : autoImplInterface.getMethods()) {
        methodIndex++;
        Request request = RequestGenerator.methodToRequest(method, dbAccess.stru(), dbAccess.conf());
        String requestFieldName = "request_" + method.getName() + '_' + methodIndex + '_' + UNIQUE_SUFFIX;

        out.println();
        out.println("  private static " + requestClassName + " create_" + requestFieldName + "() {");
        out.println("    " + requestClassName + " ret = new " + requestClassName + "();");
        RequestPrinter.print(out, "ret", request, 4);
        out.println("    return ret;");
        out.println("  }");

        out.println("  private " + requestClassName + " " + requestFieldName + " = null;");

        out.println();
        out.println("  @" + Override.class.getName());
        out.print("  public " + typeToCode(method.getGenericReturnType()) + " " + method.getName() + "(");
        int argNomer = 0;
        for (Type type : method.getGenericParameterTypes()) {
          argNomer++;
          if (argNomer > 1) {
            out.print(", ");
          }
          out.print(typeToCode(type) + " a" + argNomer);
        }
        out.println(") {");

        out.println("    " + requestClassName + " r = " + requestFieldName + ";");
        out.println("    if (r == null) r = " + requestFieldName + " = create_" + requestFieldName + "();");

        out.println("    r.result.sqlViewer = sqlViewer.get();");

        out.println("    Object []args = new Object[" + method.getGenericParameterTypes().length + "];");
        for (int i = 0, n = method.getGenericParameterTypes().length; i < n; i++) {
          out.println("    args[" + i + "] = a" + (i + 1) + ";");
        }

        out.println("    " + fcName + " fc = new " + fcName
          + "(dbAccess.get().conf(), dbAccess.get().stru(), dbAccess.get().jdbc(), r, args);");

        if (request.callNow) {

          out.println("    return (" + classNameToCodeName(method.getReturnType().getName()) + ")fc.last();");
        } else {
          out.println("    return fc;");
        }

        out.println("  }");
      }

      out.println("}");

    }

    return new GenResult() {
      @Override
      public File javaFile() {
        return file;
      }

      @Override
      public String implClassName() {
        return implClassName;
      }
    };
  }

}
