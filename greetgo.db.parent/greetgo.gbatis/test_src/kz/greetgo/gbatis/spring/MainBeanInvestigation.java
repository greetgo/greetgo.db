package kz.greetgo.gbatis.spring;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;

import kz.greetgo.gbatis.util.AbstractWithDbTest;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MainBeanInvestigation extends AbstractWithDbTest {
  @Override
  protected URL getStruUrl() {
    return getClass().getResource("stru.nf3");
  }
  
  @BeforeMethod
  public void setup() throws Exception {
    prepareConf();
    prepareSG();
    prepareDb();
  }
  
  @Test
  public void mainBeanInvestigationTest() throws Exception {
    AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath:kz/greetgo/gbatis/spring/config.xml");
    
    ctx.close();
    assertThat(1);
  }
}
