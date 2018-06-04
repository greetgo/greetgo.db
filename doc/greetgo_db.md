
# greetgo.db

Предоставляет возможность создания прокси-оболочки для автоматического открытия транзакции и её комита.
Сделано по образу и подобию Spring-а.

В String-е можно пометить какой-нибудь бин аннотацией @Transactional и все методы в нём будут вызываться в транзации.

Эта библиотека предоставляет такую возможность без привязки в какой-нибудь Dependency Injection.

В Spring-е этот бин должен быть обязательно подключён через интерфейс - желательно этот подход оставлять и здесь.
Хотя можно подключить cglib и обойтись без интерфейса.

В Spring-е предоставляется класс JdbcTemplate для доступа к БД. Здесь данный класс назван просто - Jdbc.

Основным классом, предоставляемым библиотекой, является DbProxyFactory.

Имея DbProxyFactory можно вызвать метод createProxyFor и получить проксированный объект, в котором автоматически будут
открываться и комититься транзакции.

Транзакция будет комититься, если метод исполнился без ошибки. Также, можно настроить, чтобы некоторые ошибки тоже
вызывали комит транзакции. Для этого можно воспользоваться аннотацией @CommitOn и указать в ней класс-исключение, при
генерации которого будет проходить комит.

При возникновении ошибок, будет происходить автоматический откат транзакции (rollback). Исключением являются ошибки
указанные в аннотации @CommitOn.

Аннотацией @CommitOn можно пометить интерфейс, класс-реализацию этого интерфейса, либо метод интерфейса или
метод-реализацию интерфейса.

# Quick Start

Привер использования можно посмотреть
[здесь:(QuickStart__GreetgoDb.java)...](../greetgo.nf36.gen.examples/quick_start/src/nf36_postgres_quick_start/QuickStart__GreetgoDb.java)

Там ключевым моментом являются следующие строки:

```java
public class QuickStart__GreetgoDb {
  public static void main(String[] args) throws Exception {
    //....
    
    TransactionManager transactionManager = new GreetgoTransactionManager();
    
    Jdbc jdbc = createJdbcFrom(pool, transactionManager);
    
    RegisterImpl wrappingRegister = new RegisterImpl(jdbc);
    
    DbProxyFactory dbProxyFactory = new DbProxyFactory(transactionManager);
    
    RegisterInterface proxy = dbProxyFactory.createProxyOn(RegisterInterface.class, wrappingRegister);
    
    //      at now you can use `proxy` with automatic transactions
    
    List<Thread> threadList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      threadList.add(new Thread(() -> {
        Random random = new Random();
        for (int u = 0; u < 100; u++) {
          int from = random.nextInt(10) + 1;
          int to = random.nextInt(10) + 1;
          int amount = random.nextInt(100) + 1;
    
          proxy.move(from, to, amount);
        }
      }));
    }
    
    threadList.forEach(Thread::start);
    
    //....
    
  }
}
```
