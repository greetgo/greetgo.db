package kz.greetgo.db.example.infrastructure;

import kz.greetgo.db.example.beans.all.BeanConfigAll;
import kz.greetgo.db.example.register_impl.BeanConfigRegister;
import kz.greetgo.depinject.core.BeanConfig;
import kz.greetgo.depinject.core.Include;

@BeanConfig
@Include({
  BeanConfigRegister.class,
  BeanConfigAll.class,
})
public class BeanConfigTests {}
