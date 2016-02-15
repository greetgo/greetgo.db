package kz.greetgo.gbatis.probes.asd;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.testng.annotations.Test;

public class AsdIfaceTest {
  @Test
  public void test1() throws Exception {
    assertThat(1);
    
    Method[] methods = AsdIfaceChild.class.getMethods();
    
    Method meth = methods[0];
    
    Type genericReturnType = meth.getGenericReturnType();
    ParameterizedType ptype = (ParameterizedType)genericReturnType;
    System.out.println("ptype = " + ptype);
    
    Class<?> retType = meth.getReturnType();
    System.out.println("retType = " + retType);
  }
}
