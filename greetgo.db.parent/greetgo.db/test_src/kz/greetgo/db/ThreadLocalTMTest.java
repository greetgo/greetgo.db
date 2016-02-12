package kz.greetgo.db;

import org.testng.annotations.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;

import static java.util.Arrays.asList;
import static kz.greetgo.db.ThreadLocalTM.freeConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class ThreadLocalTMTest {

  @Test
  public void freeConnection_001() throws Exception {

    Connection c1 = testProxy("c1");
    Connection c2 = testProxy("c2");
    Connection c3 = testProxy("c3");
    Connection c4 = testProxy("c3");

    List<Connection> list = asList(c1, c2, c3, c4);

    //
    //
    final int res = freeConnection(list, c2, 2);
    //
    //

    assertThat(res).isEqualTo(1);
    assertThat(list).isEqualTo(asList(c1, c2, c3, c4));

  }

  @Test
  public void freeConnection_002() throws Exception {

    Connection c1 = testProxy("c1");
    Connection c2 = testProxy("c2");
    Connection c3 = testProxy("c3");
    Connection c4 = testProxy("c4");
    Connection c5 = testProxy("c5");
    Connection c6 = testProxy("c6");
    Connection c7 = testProxy("c7");

    List<Connection> list = asList(c1, c2, c3, c4, c5, c6, c7);

    //
    //
    final int res = freeConnection(list, c3, 3);
    //
    //

    assertThat(res).isEqualTo(2);
    assertThat(list).isEqualTo(asList(c1, c2, c3, c4, c5, c6, c7));

  }

  @Test
  public void freeConnection_003() throws Exception {

    Connection c1 = testProxy("c1");
    Connection c2 = testProxy("c2");
    Connection c3 = testProxy("c3");
    Connection c4 = testProxy("c4");
    Connection c5 = testProxy("c5");
    Connection c6 = testProxy("c6");
    Connection c7 = testProxy("c7");

    List<Connection> list = asList(c1, c2, c3, c4, c5, c6, c7);

    //
    //
    final int res = freeConnection(list, c2, 4);
    //
    //

    assertThat(res).isEqualTo(3);
    assertThat(list).isEqualTo(asList(c1, c3, c4, c2, c5, c6, c7));

  }

  @Test
  public void freeConnection_004() throws Exception {

    Connection c1 = testProxy("c1");
    Connection c2 = testProxy("c2");
    Connection c3 = testProxy("c3");
    Connection c4 = testProxy("c4");
    Connection c5 = testProxy("c5");
    Connection c6 = testProxy("c6");
    Connection c7 = testProxy("c7");

    List<Connection> list = asList(c2, c3, c4, c5, c6, c7);

    //
    //
    final int res = freeConnection(list, c1, 4);
    //
    //

    assertThat(res).isLessThan(0);
    assertThat(list).isEqualTo(asList(c2, c3, c4, c5, c6, c7));

  }

  private Connection testProxy(final String name) {
    return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class},
      new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

          if (method.getParameterTypes().length == 1 && method.getName().equals("equals")) {
            return System.identityHashCode(proxy) == System.identityHashCode(args[0]);
          }

          if (method.getParameterTypes().length == 0 && method.getName().equals("toString")) {
            return name;
          }

          System.out.println("Call " + method);

          return null;
        }
      }
    );
  }
}
