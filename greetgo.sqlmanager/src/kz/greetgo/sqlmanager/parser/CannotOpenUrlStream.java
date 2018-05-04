package kz.greetgo.sqlmanager.parser;

import java.net.URL;

public class CannotOpenUrlStream extends RuntimeException {
  
  public final URL url;
  
  public CannotOpenUrlStream(URL url, Exception e) {
    super(url.toString(), e);
    this.url = url;
  }
  
}
