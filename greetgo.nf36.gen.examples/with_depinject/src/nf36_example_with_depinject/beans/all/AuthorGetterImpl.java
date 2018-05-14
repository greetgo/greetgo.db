package nf36_example_with_depinject.beans.all;

import kz.greetgo.depinject.core.Bean;
import nf36_example_with_depinject.util.AuthorGetter;

@Bean
public class AuthorGetterImpl implements AuthorGetter {

  public String author;

  @Override
  public String getAuthor() {
    return author;
  }
}
