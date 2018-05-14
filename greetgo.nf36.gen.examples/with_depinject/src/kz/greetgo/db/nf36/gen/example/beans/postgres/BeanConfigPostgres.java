package kz.greetgo.db.nf36.gen.example.beans.postgres;

import kz.greetgo.db.nf36.gen.example.beans.all.BeanConfigAll;
import kz.greetgo.depinject.core.BeanConfig;
import kz.greetgo.depinject.core.BeanScanner;
import kz.greetgo.depinject.core.Include;

@BeanConfig
@BeanScanner
@Include(BeanConfigAll.class)
public class BeanConfigPostgres {}
