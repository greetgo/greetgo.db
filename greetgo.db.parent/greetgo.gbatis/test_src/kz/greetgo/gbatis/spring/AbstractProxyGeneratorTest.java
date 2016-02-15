package kz.greetgo.gbatis.spring;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kz.greetgo.gbatis.spring.beans.TestProxyGenerator;
import kz.greetgo.gbatis.spring.beans.UsingDao;
import kz.greetgo.gbatis.spring.daos.Client;
import kz.greetgo.gbatis.spring.daos.ClientDao6;
import kz.greetgo.gbatis.util.AbstractWithDbTest;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbstractProxyGeneratorTest extends AbstractWithDbTest {
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
  public void lastTest() throws Exception {
    AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath:kz/greetgo/gbatis/spring/config.xml");
    
    {
      TestProxyGenerator x = ctx.getBean(TestProxyGenerator.class);
      x.conf = conf;
      x.stru = sg.stru;
    }
    
    ctx.getBean(UsingDao.class).prepareClients();
    
    {
      ClientDao6 clientDao6 = ctx.getBean(ClientDao6.class);
      
      System.out.println("##################################");
      List<Client> youngClients = clientDao6.youngClients(14).last();
      
      for (Client client : youngClients) {
        System.out.println(client);
      }
      assertThat(youngClients).hasSize(5);
      System.out.println("##################################");
    }
    
    ctx.close();
  }
  
  @Test
  public void atTest() throws Exception {
    AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath:kz/greetgo/gbatis/spring/config.xml");
    
    {
      TestProxyGenerator x = ctx.getBean(TestProxyGenerator.class);
      x.conf = conf;
      x.stru = sg.stru;
    }
    
    UsingDao usingDao = ctx.getBean(UsingDao.class);
    usingDao.prepareClients();
    
    ClientDao6 clientDao6 = ctx.getBean(ClientDao6.class);
    
    Thread.sleep(500);
    
    Date beforeOlding = clientDao6.now();
    System.out.println("beforeOlding = " + beforeOlding);
    
    Thread.sleep(100);
    
    usingDao.makeOlds();
    
    List<Client> youngClientsLast = clientDao6.youngClients(17).last();
    
    List<Client> youngClientsAt = clientDao6.youngClients(17).at(beforeOlding);
    
    System.out.println("youngClientsLast.size = " + youngClientsLast.size());
    System.out.println("youngClientsAt.size = " + youngClientsAt.size());
    
    assertThat(youngClientsLast).hasSize(3);
    assertThat(youngClientsAt).hasSize(8);
    
    for (Client client : youngClientsAt) {
      System.out.println("!!!!!!!! " + client);
    }
    System.out.println("**************");
    for (Client client : clientDao6.youngClients(17).at(beforeOlding, 4, 3)) {
      System.out.println("!!!!!!!! " + client);
    }
    
    List<String> surnames = clientDao6.youngClientsField(17, "surname").at(beforeOlding);
    
    System.out.println("surnames = " + surnames);
    
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      Date atNowMinus3 = clientDao6.nowMinus3();
      Date atNow = clientDao6.now();
      System.out.println("now - 3 = " + sdf.format(atNowMinus3));
      System.out.println("now     = " + sdf.format(atNow));
    }
    
    {
      Boolean bool = clientDao6.getFalse().last();
      assertThat(bool).isNotNull();
      assertThat(bool).isFalse();
    }
    
    ctx.close();
  }
}
