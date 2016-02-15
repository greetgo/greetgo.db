package kz.greetgo.gbatis.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.greetgo.gbatis.classscanner.ClassScanner;
import kz.greetgo.gbatis.classscanner.ClassScannerDef;
import kz.greetgo.gbatis.futurecall.FutureCallDef;
import kz.greetgo.gbatis.futurecall.SqlViewer;
import kz.greetgo.gbatis.model.Request;
import kz.greetgo.gbatis.modelreader.RequestGenerator;
import kz.greetgo.gbatis.t.Autoimpl;
import kz.greetgo.sqlmanager.gen.Conf;
import kz.greetgo.sqlmanager.model.Stru;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Инициализирует spring объектами из gbatis-а
 * 
 * @author pompei
 */
public abstract class AbstractProxyGenerator implements BeanDefinitionRegistryPostProcessor,
    ApplicationContextAware {
  
  private ApplicationContext appContext;
  
  protected abstract Conf getConf() throws Exception;
  
  protected abstract Stru getStru() throws Exception;
  
  protected abstract JdbcTemplate getJdbcTemplate() throws Exception;
  
  protected abstract List<String> getBasePackageList();
  
  protected SqlViewer getSqlViewer() {
    return null;
  }
  
  protected ClassScanner getClassScanner() {
    return new ClassScannerDef();
  }
  
  @SuppressWarnings("unused")
  private static final Log LOG = LogFactory.getLog(AbstractProxyGenerator.class);
  
  public ApplicationContext getAppContext() {
    return appContext;
  }
  
  @Override
  public void setApplicationContext(ApplicationContext appContext) throws BeansException {
    this.appContext = appContext;
  }
  
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {}
  
  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    for (String packageName : getBasePackageList()) {
      IN: for (Class<?> class1 : getClassScanner().scanPackage(packageName)) {
        if (!class1.isInterface()) continue IN;
        if (class1.getAnnotation(Autoimpl.class) == null) continue IN;
        generateAndRegisterProxy(class1, registry);
      }
    }
  }
  
  private void generateAndRegisterProxy(final Class<?> class1, BeanDefinitionRegistry registry) {
    Class<?> proxyClass = Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { class1 });
    
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(proxyClass);
    builder.addConstructorArgValue(new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invokeMethod(class1, proxy, method, args);
      }
    });
    AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
    
    String namePrefix = "gbatis-autoimpl-" + class1.getName() + "-";
    for (int i = 1; true; i++) {
      
      String name = namePrefix + i;
      
      if (!registry.isBeanNameInUse(name)) {
        registry.registerBeanDefinition(name, beanDefinition);
        return;
      }
    }
    
  }
  
  private Object invokeMethod(Class<?> iface, Object proxy, Method method, Object[] args)
      throws Exception {
    if ("toString".equals(method.getName()) && method.getParameterTypes().length == 0) {
      return proxy.getClass().getName() + "-for-interface-" + iface.getName() + "@"
          + System.identityHashCode(proxy);
    }
    
    Request request = getRequest(iface, method);
    
    FutureCallDef<Object> futureCall = new FutureCallDef<Object>(getConf(), getStru(),
        getJdbcTemplate(), request, args);
    
    futureCall.request.result.sqlViewer = getSqlViewer();
    
    if (request.callNow) return futureCall.last();
    
    return futureCall;
  }
  
  private final Map<String, Request> requestCache = new HashMap<>();
  
  private Request getRequest(Class<?> iface, Method method) throws Exception {
    String id = iface.getName() + " : " + method.toGenericString();
    
    synchronized (requestCache) {
      {
        Request request = requestCache.get(id);
        if (request != null) return request;
      }
      {
        Request request = RequestGenerator.methodToRequest(method, getStru(), getConf());
        requestCache.put(id, request);
        return request;
      }
    }
  }
  
}
