package org.monet.grided.core.util;

import java.io.InputStream;

public class Resources {

  public static InputStream getAsStream(String name) {
    return Resources.class.getResourceAsStream(name);
  }
  
}
