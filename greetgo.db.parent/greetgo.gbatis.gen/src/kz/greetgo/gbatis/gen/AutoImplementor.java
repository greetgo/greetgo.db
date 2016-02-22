package kz.greetgo.gbatis.gen;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.gbatis.futurecall.FutureCallImpl;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.modelreader.RequestGenerator;
import kz.greetgo.gbatis.t.DbAccessInfo;
import kz.greetgo.util.ServerUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
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

  public static final String IMPL_POSTFIX = "GeneratedImplementation";
  private static final String METHOD_UNIQUE_POSTFIX = "7264364843";

  public GenResult generate(Class<?> autoImplInterface) throws Exception {
    final String implClassName = autoImplInterface.getName() + IMPL_POSTFIX;

    final File file = new File(srcDir + '/' + implClassName.replace('.', '/') + ".java");

    dummyCheck(file.getParentFile().mkdirs());

    final String requestClassName = Request.class.getName();
    final String fcName = FutureCallImpl.class.getName();
    final String bgName = BeanGetter.class.getName();

    final MessageDigest digest = MessageDigest.getInstance("MD5");

    final FileOutputStream fileOutputStream = new FileOutputStream(file);
    final OutputStream outputStream = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        throw new RuntimeException("Cannot be called");
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        fileOutputStream.write(b, off, len);
        digest.update(b, off, len);
      }

      @Override
      public void close() throws IOException {
        fileOutputStream.close();
      }
    };
    final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
    final BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

    try (final PrintWriter out = new PrintWriter(bufferedWriter)) {
      out.println("package " + autoImplInterface.getPackage().getName() + ";");
      out.println();

      out.println("@" + Bean.class.getName());
      out.println("public class " + extractName(implClassName) + " implements " + autoImplInterface.getName() + " {");

      out.println("  public " + bgName + "<" + SqlViewer.class.getName() + "> sqlViewer;");
      out.println("  public " + bgName + "<" + classNameToCodeName(dbAccess.getClass().getName()) + "> dbAccess;");

      List<Method> methodList = new ArrayList<>();
      Collections.addAll(methodList, autoImplInterface.getMethods());
      Collections.sort(methodList, new Comparator<Method>() {
        @Override
        public int compare(Method o1, Method o2) {
          return o1.toGenericString().compareTo(o2.toGenericString());
        }
      });

      int methodIndex = 0;
      for (Method method : methodList) {
        methodIndex++;
        Request request = RequestGenerator.methodToRequest(method, dbAccess.stru(), dbAccess.conf());
        String requestFieldName = "request_" + method.getName() + '_' + methodIndex + '_' + METHOD_UNIQUE_POSTFIX;

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
          if (Void.TYPE == method.getReturnType()) {
            out.println("    fc.last();");
            out.println("    return;");
          } else {
            out.println("    return (" + classNameToCodeName(method.getReturnType().getName()) + ")fc.last();");
          }
        } else {
          out.println("    return fc;");
        }

        out.println("  }");
      }

      out.println("}");
    }

    final String hashJustCalculated = printBase64Binary(digest.digest());

    final File hashFile = new File(file.getPath() + "." + digest.getAlgorithm().toLowerCase());

    boolean writeHashFile = true;

    if (hashFile.exists()) {
      final String hashFromFile = ServerUtil.streamToStr(new FileInputStream(hashFile));
      if (hashFromFile.equals(hashJustCalculated)) {
        writeHashFile = false;
      }
    }

    if (writeHashFile) try (final PrintWriter out = new PrintWriter(hashFile, "UTF-8")) {
      out.print(hashJustCalculated);
    }

    return new GenResult() {
      @Override
      public File javaFile() {
        return file;
      }

      @Override
      public File hashFile() {
        return hashFile;
      }

      @Override
      public String implClassName() {
        return implClassName;
      }
    };
  }

}
