package kz.greetgo.db;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProbeEnhancer {

  static class Wow {

    public void asd() {
      System.out.println("from asd");
    }

  }

  public static void main(String[] args) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(Wow.class);

    enhancer.setCallback(new MethodInterceptor() {
      @Override
      public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        if (method.getParameterTypes().length == 0 && method.getName().equals("toString")) {
          return "wow proxy@" + System.identityHashCode(obj);
        }

        //System.out.println("obj = " + obj);
        return null;
      }
    });

    final Wow wowProxy = (Wow) enhancer.create();

    System.out.println("wowProxy = " + wowProxy);


  }
}

