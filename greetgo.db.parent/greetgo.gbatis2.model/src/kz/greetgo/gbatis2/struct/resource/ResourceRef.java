package kz.greetgo.gbatis2.struct.resource;

import java.io.InputStream;

public interface ResourceRef {
  InputStream getInputStream();

  ResourceRef change(String path);

  String display();
}
