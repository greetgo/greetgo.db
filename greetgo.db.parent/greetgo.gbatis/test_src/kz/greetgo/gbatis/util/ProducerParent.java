
package kz.greetgo.gbatis.util;

import java.util.List;

public abstract class ProducerParent {

  protected abstract String __topic__();

  protected void __goListFromSender__(List<Object> list) throws Exception {

  }
}