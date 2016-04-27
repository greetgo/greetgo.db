package kz.greetgo.gbatis.util.impl;

import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static java.lang.System.identityHashCode;
import static org.fest.assertions.api.Assertions.assertThat;

public class ClassInfoTest {

  @SuppressWarnings("unused")
  public static class Client {
    public String id;


    @SuppressWarnings("SameReturnValue")
    public String getName() {
      return "Hello name";
    }

    public String wasSetName = null;

    public void setName(String name) {
      wasSetName = name;
    }

    public boolean forReturnIsSinus;

    public boolean isSinus() {
      return forReturnIsSinus;
    }

    public boolean forReturnIsCoSinus;

    public Boolean isCoSinus() {
      return forReturnIsCoSinus;
    }
  }

  @Test
  public void readField() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    client.id = "abra 295";
    assertThat(classAcceptor.get(client, "id")).isEqualTo("abra 295");
  }

  @Test
  public void writeField() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    classAcceptor.set(client, "id", "Hello");
    assertThat(client.id).isEqualTo("Hello");

  }

  @Test
  public void readMethod() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    assertThat(classAcceptor.get(client, "name")).isEqualTo("Hello name");
  }


  @Test
  public void writeMethod() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    classAcceptor.set(client, "name", "Insert world");
    assertThat(client.wasSetName).isEqualTo("Insert world");
  }

  @Test
  public void cache() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    ClassAcceptor classAcceptor2 = classInfo.getClassAcceptor(Client.class);
    assertThat(identityHashCode(classAcceptor2)).isEqualTo(identityHashCode(classAcceptor));
  }

  @Test
  public void readMethod_boolean() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    client.forReturnIsSinus = RND.bool();
    assertThat(classAcceptor.get(client, "sinus")).isEqualTo(client.forReturnIsSinus);
  }

  @Test
  public void readMethod_Boolean() throws Exception {
    ClassInfo classInfo = new ClassInfo();
    ClassAcceptor classAcceptor = classInfo.getClassAcceptor(Client.class);
    Client client = new Client();
    client.forReturnIsCoSinus = RND.bool();
    assertThat(classAcceptor.get(client, "coSinus")).isEqualTo(client.forReturnIsCoSinus);
  }
}
