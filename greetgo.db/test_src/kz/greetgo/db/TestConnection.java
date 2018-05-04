package kz.greetgo.db;

import kz.greetgo.util.RND;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class TestConnection implements InvocationHandler {

  public final String name = "TestConnection(" + RND.str(10) + ")";

  public int closeCallCount = 0;
  public int setAutoCommitCallCount = 0;
  public boolean autoCommit = true;
  public int transactionIsolation = -1;
  public int setTransactionIsolationCallCount = 0;

  public int commitCallCount = 0, rollbackCallCount = 0;

  public final List<String> events = new ArrayList<>();

  public static String transactionIsolationToStr(int transactionIsolationCode) {
    switch (transactionIsolationCode) {
      case Connection.TRANSACTION_READ_COMMITTED:
        return "READ_COMMITTED";

      case Connection.TRANSACTION_READ_UNCOMMITTED:
        return "READ_UNCOMMITTED";

      case Connection.TRANSACTION_REPEATABLE_READ:
        return "REPEATABLE_READ";

      case Connection.TRANSACTION_SERIALIZABLE:
        return "SERIALIZABLE";

      case -1:
        return "NO_ISOLATION_LEVEL";

      default:
        throw new IllegalArgumentException("Unknown transactionIsolationCode = " + transactionIsolationCode);
    }

  }

  public String transactionIsolationStr() {
    return transactionIsolationToStr(transactionIsolation);
  }

  public static TestConnection extractTestConnection(Object proxyOrNot) {
    assertThat(proxyOrNot).isNotNull();
    try {
      InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxyOrNot);
      if (invocationHandler instanceof TestConnection) return (TestConnection) invocationHandler;
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    final String methodName = method.getName();

    if (method.getParameterTypes().length == 0 && methodName.equals("close")) {
      events.add("CLOSE");
      closeCallCount++;
      return null;
    }

    if (method.getParameterTypes().length == 1 && methodName.equals("equals")) {
      if (proxy == null && args[0] == null) return true;
      if (proxy == null) return false;
      if (args[0] == null) return false;
      {
        TestConnection tc1 = extractTestConnection(proxy);
        TestConnection tc2 = extractTestConnection(args[0]);
        if (tc1 == null && tc2 == null) return System.identityHashCode(proxy) == System.identityHashCode(args[0]);
        if (tc1 == null) return false;
        if (tc2 == null) return false;
        return tc1.name.equals(tc2.name);
      }
    }

    if (method.getParameterTypes().length == 1 && methodName.equals("setAutoCommit")) {
      autoCommit = (boolean) args[0];
      setAutoCommitCallCount++;
      events.add("SET AutoCommit TO " + autoCommit);
      return null;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("getTransactionIsolation")) {
      return transactionIsolation;
    }

    if (method.getParameterTypes().length == 1 && methodName.equals("setTransactionIsolation")) {
      transactionIsolation = (int) args[0];
      setTransactionIsolationCallCount++;
      events.add("SET TransactionIsolation TO " + transactionIsolationToStr(transactionIsolation));
      return null;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("getSchema")) {
      return this.name;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("getAutoCommit")) {
      return autoCommit;
    }


    if (method.getParameterTypes().length == 0 && methodName.equals("isClosed")) {
      return closeCallCount > 0;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("commit")) {
      commitCallCount++;
      events.add("COMMIT");
      return null;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("rollback")) {
      rollbackCallCount++;
      events.add("ROLLBACK");
      return null;
    }

    if (method.getParameterTypes().length == 1 && methodName.equals("prepareStatement")) {
      events.add("CALL prepareStatement(" + args[0] + ")");
      return null;
    }

    if (method.getParameterTypes().length == 0 && methodName.equals("toString")) {
      return "proxyOf(" + Connection.class.getName() + ")@" + System.identityHashCode(proxy);
    }

    throw new RuntimeException("Cannot invoke method " + method);
  }


}
