package kz.greetgo.db.nf36;

import kz.greetgo.db.nf36.gen.example.structure.Client;
import kz.greetgo.db.nf36.gen.example.structure.Street;
import kz.greetgo.db.nf36.gen.example.structure.inner.Chair;
import kz.greetgo.db.nf36.gen.example.structure.inner.ClientAddress;
import kz.greetgo.db.nf36.gen.example.structure.inner.Wow;
import kz.greetgo.db.nf36.model.Nf3Table;

import java.io.File;
import java.util.List;

public class GenerateExample {
  public static void main(String[] args) {
    List<Nf3Table> nf3TableList = ModelCollector
      .newCollector()
      .setNf3Prefix(/*empty*/"")
      .setNf6Prefix("m_")
      .setEnumLength(50)
      .register(new Client())
      .register(new ClientAddress())
      .register(new Street())
      .register(new Chair())
      .register(new Wow())
      .collect();

    JavaGenerator.newGenerator()
      .setInterfaceOutDir("left 1")
      .setImplOutDir("left 2")
      .setOutDir("greetgo.nf36.gen.example/src")
      .setCleanOutDirsBeforeGeneration(true)
      .setInterfaceBasePackage("kz.greetgo.db.nf36.gen.example.generated.faces")
      .setImplBasePackage("kz.greetgo.db.nf36.gen.example.generated.impl")
      .setSourceBasePackage(Client.class.getPackage().getName())
      .setMainNf36ClassName("TestNf3Door")
      .setMainNf36ClassAbstract(true)
      .setNf3TableList(nf3TableList)
      .generate()
    ;

    DdlGenerator.newGenerator()
      .setNf3TableList(nf3TableList)
      .setSqlDialect(new SqlDialectPostgres())
      .setCommandSeparator(";;;;")
      .generateCreateTables(new File("greetgo.nf36.gen.example/build/create_tables.sql"))
    ;
  }
}
