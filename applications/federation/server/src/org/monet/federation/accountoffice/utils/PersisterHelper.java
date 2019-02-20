package org.monet.federation.accountoffice.utils;

import org.simpleframework.xml.core.Persister;

public class PersisterHelper {

  private static Persister persister = new Persister();

  public static <T> T load(String content, Class<T> objectClazz) throws Exception {
    return persister.read(objectClazz, content);
  }

}
