
## greetgo.nf36

Предоставляет возможность синхронной работы с 3-ей и 6-ой нормальными формами в БД.

3-я нормальная форма используется для работы с оперативными данными. В 6-ю нормальную форму записываются все изменения.

В 6-ой нормальной форме сохраняется кто и когда менял тот или иной параметр (автор изменений и время изменений).

6-я нормальная форма обновляется синхронно в транзакционном режиме автоматически.

Вы просто говорите системе: вот в таком субъекте (с таким идентификатором) изменились такие и такие поля
на такие и такие значения - система меняет значения в 3-ей нормальной форме и добавляет строки в 6-ю нормальную фому,
при этом автоматически записывает время изменений и авторма изменений: автор берётся автоматически из сессии.

Например у нас имеется субъект Client. У него есть строковый идентификатор (id) и три параметра: surname, name
и patronymic. Вы рассказываете это системе и система автоматически генерирует следующие таблицы:

```sql
-- Диалект PostgreSQL:

create table Client (
  id         varchar(32) primary key,
  surname    varchar(300),
  name       varchar(300),
  patronymic varchar(300)
);

create table m_client_surname (
  id      varchar(32) not null references Client,
  ts      timestamp not null default clock_timestamp(),
  surname varchar(300),
  author  varchar(32)
);

create table m_client_name (
  id     varchar(32) not null references Client,
  ts     timestamp not null default clock_timestamp(),
  name   varchar(300),
  author varchar(32)
);

create table m_client_patronymic (
  id         varchar(32) not null references Client,
  ts         timestamp not null default clock_timestamp(),
  patronymic varchar(300),
  author     varchar(32)
);
```
