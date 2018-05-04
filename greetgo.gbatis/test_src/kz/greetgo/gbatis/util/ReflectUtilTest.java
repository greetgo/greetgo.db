package kz.greetgo.gbatis.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Map;

import kz.greetgo.util.RND;
import org.testng.annotations.Test;

public class ReflectUtilTest {
  
  @SuppressWarnings("unused")
  static class Asd {
    public String fieldStr;
    
    public Integer fieldInteger;
    
    public int fieldInt;
    
    public String propertyStr;
    
    public void setPropertyStr(String value) {
      propertyStr = value + "-prop";
    }
    
    public Integer leftSetterType;
    
    public void setLeftSetterType(String value) {
      leftSetterType = Integer.parseInt(value) + 303;
    }
    
    public int propertyInt;
    
    public void setPropertyInt(int value) {
      propertyInt = value + 781;
    }
  }
  
  @Test
  public void scanSetters_fieldStr() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    String value = RND.str(10);
    
    setters.get("fieldStr").set(value);
    
    assertThat(asd.fieldStr).isEqualTo(value);
  }
  
  @Test
  public void scanSetters_fieldInteger() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    Integer value = RND.plusInt(10000000);
    
    setters.get("fieldInteger").set(value);
    
    assertThat(asd.fieldInteger).isEqualTo(value);
  }
  
  @Test
  public void scanSetters_fieldInt() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    int value = RND.plusInt(10000000);
    
    setters.get("fieldInt").set(value);
    
    assertThat(asd.fieldInt).isEqualTo(value);
  }
  
  @Test
  public void scanSetters_propertyStr() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    String value = RND.str(10);
    
    setters.get("propertyStr").set(value);
    
    assertThat(asd.propertyStr).isEqualTo(value + "-prop");
  }
  
  @Test
  public void scanSetters_leftSetterType() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    int value = RND.plusInt(10000000);
    
    setters.get("leftSetterType").set("" + value);
    
    assertThat(asd.leftSetterType).isEqualTo(value + 303);
  }
  
  @Test
  public void scanSetters_propertyInt() {
    Asd asd = new Asd();
    Map<String, Setter> setters = ReflectUtil.scanSetters(asd);
    
    assertThat(setters).isNotNull();
    
    int value = RND.plusInt(10000000);
    
    setters.get("propertyInt").set(value);
    
    assertThat(asd.propertyInt).isEqualTo(value + 781);
  }
}
