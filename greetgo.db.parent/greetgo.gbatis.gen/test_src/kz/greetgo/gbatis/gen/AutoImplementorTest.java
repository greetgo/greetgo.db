package kz.greetgo.gbatis.gen;

import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.db.Jdbc;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.gen.gbatis.ZNF3;
import kz.greetgo.gbatis.t.DbAccessInfo;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;
import kz.greetgo.util.ServerUtil;
import org.testng.annotations.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AutoImplementorTest {

  static class TestDbAccessInfo implements DbAccessInfo {
    public TestDbAccessInfo() {
    }

    @Override
    public Conf conf() {
      return ZNF3.getConf();
    }

    @Override
    public Stru stru() {
      try {
        return ZNF3.struGen().stru;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public Jdbc jdbc() {
      return new Jdbc() {
        @Override
        public <T> T execute(ConnectionCallback<T> connectionCallback) {
          try {
            Class.forName("org.h2.Driver");
            try (final Connection con = DriverManager.getConnection("jdbc:h2:mem:test_mem", "sa", "")) {
              return connectionCallback.doInConnection(con);
            }
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      };
    }
  }

  @Test
  public void generate() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
    String srcDir = "build/autoimpl_" + sdf.format(new Date());

    final TestDbAccessInfo tda = new TestDbAccessInfo();
    AutoImplementor ai = new AutoImplementor(srcDir, tda);

    final GenResult gr = ai.generate(TestDao6.class);

    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final int exitCode = compiler.run(System.in, System.out, System.err, gr.javaFile().getPath());
    if (exitCode != 0) throw new RuntimeException("exitCode = " + exitCode);

    ServerUtil.addToClasspath(new File(srcDir));

    final Class<?> implClass = Class.forName(gr.implClassName());

    final TestDao6 testDao6impl = (TestDao6) implClass.newInstance();

    implClass.getField("sqlViewer").set(testDao6impl, new BeanGetter<Object>() {
      final SqlViewer sqlViewer = new SqlViewer() {
        @Override
        public boolean needView() {
          return true;
        }

        @Override
        public void view(String sql, List<Object> params, long delay, Exception err) {
          System.out.println(sql + ": " + params);
        }
      };

      @Override
      public Object get() {
        return sqlViewer;
      }
    });
    implClass.getField("dbAccess").set(testDao6impl, new BeanGetter<Object>() {
      @Override
      public Object get() {
        return tda;
      }
    });

    final Long res = testDao6impl.loadContractIdList(213).last();
    System.out.println("res = " + res);

    System.out.println(implClass);
  }
}