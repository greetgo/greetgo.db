package kz.greetgo.db.nf36.gen.example.beans;

import kz.greetgo.db.nf36.gen.example.util.AuthorGetter;
import kz.greetgo.depinject.core.Bean;

@Bean
public class AuthorGetterImpl implements AuthorGetter {

  public String author;

  @Override
  public String getAuthor() {
    return author;
  }
}