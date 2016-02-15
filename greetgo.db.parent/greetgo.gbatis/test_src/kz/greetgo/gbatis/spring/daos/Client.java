package kz.greetgo.gbatis.spring.daos;

public class Client {
  public long id;
  public String surname, name, pareonymic;
  public Integer age;
  
  @Override
  public String toString() {
    return id + " " + surname + " " + name + " " + pareonymic + " " + age;
  }
}
