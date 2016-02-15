package kz.greetgo.gbatis.spring.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MainBean implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
  
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {}
  
  @SuppressWarnings("unused")
  private ApplicationContext appContext;
  
  @Override
  public void setApplicationContext(ApplicationContext appContext) throws BeansException {
    this.appContext = appContext;
    
  }
  
  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    {
      InvocationHandler h = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          if ("toString".equals(method.getName()) && method.getParameterTypes().length == 0) {
            return proxy.getClass().getName() + "-for-interface-" + AsdWow1.class.getName() + "@"
                + System.identityHashCode(proxy);
          }
          System.out.println("Called method " + method);
          return null;
        }
      };
      
      Class<?> proxyClass = Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(),
          new Class<?>[] { AsdWow1.class });
      
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(proxyClass);
      builder.addConstructorArgValue(h);
      AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
      registry.registerBeanDefinition("AsdWow1-proxied-interface", beanDefinition);
    }
  }
  
}