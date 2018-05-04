package kz.greetgo.db.example.beans.all;

import kz.greetgo.db.DbProxyFactory;
import kz.greetgo.db.GreetgoTransactionManager;
import kz.greetgo.db.InTransaction;
import kz.greetgo.db.TransactionManager;
import kz.greetgo.db.example.configs.PostgresConfig;
import kz.greetgo.db.example.utils.JdbcExample;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.HasAfterInject;
import kz.greetgo.depinject.core.replace.BeanReplacer;
import kz.greetgo.depinject.core.replace.ReplaceWithAnn;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Bean
@ReplaceWithAnn(InTransaction.class)
public class ExampleJdbcFactory implements HasAfterInject, BeanReplacer {
  private final TransactionManager transactionManager = new GreetgoTransactionManager();
  private final DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);
  private JdbcExample jdbcExample = null;

  @Override
  public Object replaceBean(Object originalBean, Class<?> returnClass) {
    if (!returnClass.isInterface()) return originalBean;
    return dbProxyFactory.createProxyFor(originalBean, returnClass);
  }

  @Override
  public void afterInject() throws Exception {
    jdbcExample = new JdbcExample(createDataSource(), transactionManager);
  }

  @Bean
  public JdbcExample getJdbcExample() {
    return jdbcExample;
  }

  private DataSource createDataSource() {

    BasicDataSource pool = new BasicDataSource();

    pool.setDriverClassName("org.postgresql.Driver");
    pool.setUrl(PostgresConfig.url());
    pool.setUsername(PostgresConfig.username());
    pool.setPassword(PostgresConfig.password());

    pool.setInitialSize(0);

    return pool;
  }

}
