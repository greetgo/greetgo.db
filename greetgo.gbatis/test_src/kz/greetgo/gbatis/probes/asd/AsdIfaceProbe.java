package kz.greetgo.gbatis.probes.asd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AsdIfaceProbe {
  public static void main(String[] args) {
    new AsdIfaceProbe().run();
  }
  
  private void run() {
    InvocationHandler h = new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method = " + method);
        return null;
      }
    };
    
    AsdIfaceChild asd = (AsdIfaceChild)Proxy.newProxyInstance(Thread.currentThread()
        .getContextClassLoader(), new Class<?>[] { AsdIfaceChild.class }, h);
    
    asd.asdName(123L);
  }
}
