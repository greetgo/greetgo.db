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
    
    Method method = methods[0];
    
    Type genericReturnType = method.getGenericReturnType();
    ParameterizedType pType = (ParameterizedType)genericReturnType;
    System.out.println("pType = " + pType);
    
    Class<?> retType = method.getReturnType();
    System.out.println("retType = " + retType);
  }
}
