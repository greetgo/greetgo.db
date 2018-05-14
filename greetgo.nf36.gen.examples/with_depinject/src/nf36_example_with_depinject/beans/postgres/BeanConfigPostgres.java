package nf36_example_with_depinject.beans.postgres;

import kz.greetgo.depinject.core.BeanConfig;
import kz.greetgo.depinject.core.BeanScanner;
import kz.greetgo.depinject.core.Include;
import nf36_example_with_depinject.beans.all.BeanConfigAll;

@BeanConfig
@BeanScanner
@Include(BeanConfigAll.class)
public class BeanConfigPostgres {}
